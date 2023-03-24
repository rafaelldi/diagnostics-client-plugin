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

    internal GcEventProducer(
        int pid,
        ChannelWriter<ValueGcEvent> writer,
        Lifetime lifetime)
    {
        _pid = pid;
        _sessionManager = new EventPipeSessionManager(pid);
        _writer = writer;
        _providers = new[] { EventPipeProviderFactory.CreateGcProvider() };

        lifetime.OnTermination(() => _writer.Complete());
    }

    internal Task Produce()
    {
        var session = _sessionManager.StartSession(_providers, false);
        Lifetime.AsyncLocal.Value.AddDispose(session);

        var source = new EventPipeEventSource(session.EventStream);
        Lifetime.AsyncLocal.Value.AddDispose(source);

        var cancellationToken = Lifetime.AsyncLocal.Value.ToCancellationToken();
        cancellationToken.Register(() => EventPipeSessionManager.StopSession(session));

        source.NeedLoadedDotNetRuntimes();
        source.AddCallbackOnProcessStart(tp =>
        {
            tp.AddCallbackOnDotNetRuntimeLoad(runtime =>
                runtime.GCEnd += HandleEvent);
        });

        return Task.Run(() =>  source.Process(), Lifetime.AsyncLocal.Value);
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