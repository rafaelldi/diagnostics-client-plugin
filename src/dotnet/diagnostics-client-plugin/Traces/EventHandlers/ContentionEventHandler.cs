using System.Threading.Channels;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Parsers.Clr;

namespace DiagnosticsClientPlugin.Traces.EventHandlers;

internal sealed class ContentionEventHandler
{
    private readonly int _pid;
    private readonly ChannelWriter<ValueTrace> _writer;

    internal ContentionEventHandler(int pid, ChannelWriter<ValueTrace> writer)
    {
        _pid = pid;
        _writer = writer;
    }

    internal void SubscribeToEvents(EventPipeEventSource source)
    {
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ContentionStart += HandleContentionStartEvent,
            () => source.Clr.ContentionStart -= HandleContentionStartEvent
        );
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ContentionStop += HandleContentionStopEvent,
            () => source.Clr.ContentionStop -= HandleContentionStopEvent
        );
    }

    private void HandleContentionStartEvent(ContentionStartTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Contention Start",
            PredefinedProvider.Contentions,
            evt.TimeStamp,
            string.Empty
        );
        _writer.TryWrite(trace);
    }

    private void HandleContentionStopEvent(ContentionStopTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Contention Stop",
            PredefinedProvider.Contentions,
            evt.TimeStamp,
            $"Duration(ns): {evt.DurationNs}"
        );
        _writer.TryWrite(trace);
    }
}