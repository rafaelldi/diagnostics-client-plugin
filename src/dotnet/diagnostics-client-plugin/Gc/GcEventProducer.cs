using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.EventPipes;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.NETCore.Client;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Analysis;
using Microsoft.Diagnostics.Tracing.Analysis.GC;
using Microsoft.Diagnostics.Tracing.Parsers.Clr;

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
        _providers = new[] { EventPipeProviderFactory.CreateGcProvider() };
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
            gc.GCGenerationName.ToString(),
            gc.Reason.ToValue(),
            Math.Round(gc.PauseDurationMSec, 3),
            Math.Round(gc.HeapSizePeakMB, 3),
            Math.Round(gc.HeapSizeAfterMB, 3),
            Math.Round(gc.RatioPeakAfter, 2),
            Math.Round(gc.PromotedMB, 3),
            Math.Round(gc.AllocedSinceLastGCMB, 3),
            Math.Round(gc.AllocRateMBSec, 2),
            Math.Round(gc.GenSizeAfterMB(Gens.Gen0), 3),
            Math.Round(gc.GenFragmentationPercent(Gens.Gen0), 3),
            Math.Round(gc.SurvivalPercent(Gens.Gen0), 2),
            Math.Round(gc.GenBudgetMB(Gens.Gen0), 2),
            Math.Round(gc.GenSizeAfterMB(Gens.Gen1), 3),
            Math.Round(gc.GenFragmentationPercent(Gens.Gen1), 3),
            Math.Round(gc.SurvivalPercent(Gens.Gen1), 2),
            Math.Round(gc.GenBudgetMB(Gens.Gen1), 2),
            Math.Round(gc.GenSizeAfterMB(Gens.Gen2), 3),
            Math.Round(gc.GenFragmentationPercent(Gens.Gen2), 3),
            Math.Round(gc.SurvivalPercent(Gens.Gen2), 2),
            Math.Round(gc.GenBudgetMB(Gens.Gen2), 2),
            Math.Round(gc.GenSizeAfterMB(Gens.GenLargeObj), 3),
            Math.Round(gc.GenFragmentationPercent(Gens.GenLargeObj), 3),
            Math.Round(gc.SurvivalPercent(Gens.GenLargeObj), 2),
            Math.Round(gc.GenBudgetMB(Gens.GenLargeObj), 2),
            gc.HeapStats.PinnedObjectCount
        );

        _writer.TryWrite(gcEvent);
    }
}