using System.Diagnostics.Tracing;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.EventPipes;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.NETCore.Client;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Analysis;
using Microsoft.Diagnostics.Tracing.Analysis.GC;
using Microsoft.Diagnostics.Tracing.Parsers;

namespace DiagnosticsClientPlugin.Gc;

internal sealed class GcEventProducer
{
    private readonly int _pid;
    private readonly EventPipeSessionManager _sessionManager;
    private readonly ChannelWriter<ValueGcEvent> _writer;
    private readonly EventPipeProvider[] _providers;
    private readonly Lifetime _lt;

    internal GcEventProducer(
        int pid,
        ChannelWriter<ValueGcEvent> writer,
        Lifetime lt)
    {
        _pid = pid;
        _sessionManager = new EventPipeSessionManager(pid);
        _writer = writer;
        _providers = new[]
        {
            new EventPipeProvider(
                Providers.DotNetRuntimeProvider,
                EventLevel.Informational,
                (long)ClrTraceEventParser.Keywords.GC)
        };
        _lt = lt;

        _lt.OnTermination(() => _writer.Complete());
    }

    internal Task Produce(Lifetime lt)
    {
        var lifetime = _lt.Intersect(lt);

        var session = _sessionManager.StartSession(_providers, false);
        lifetime.AddDispose(session);

        var source = new EventPipeEventSource(session.EventStream);
        lifetime.AddDispose(source);

        lifetime.OnTermination(() => EventPipeSessionManager.StopSession(session));

        source.NeedLoadedDotNetRuntimes();
        source.AddCallbackOnProcessStart(tp =>
        {
            tp.AddCallbackOnDotNetRuntimeLoad(runtime =>
                runtime.GCEnd += (process, gc) => HandleEvent(process, gc));
        });

        return Task.Run(() => { source.Process(); }, lifetime);
    }

    private void HandleEvent(TraceProcess process, TraceGC gc)
    {
        if (process.ProcessID != _pid)
        {
            return;
        }

        var gcEvent = new ValueGcEvent(
            gc.Number,
            gc.Generation
        );

        _writer.TryWrite(gcEvent);
    }
}