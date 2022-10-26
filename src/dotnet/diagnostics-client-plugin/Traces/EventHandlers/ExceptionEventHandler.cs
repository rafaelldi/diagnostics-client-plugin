using System.Threading.Channels;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Parsers.Clr;

namespace DiagnosticsClientPlugin.Traces.EventHandlers;

internal sealed class ExceptionEventHandler
{
    private readonly int _pid;
    private readonly ChannelWriter<ValueTrace> _writer;

    internal ExceptionEventHandler(int pid, ChannelWriter<ValueTrace> writer)
    {
        _pid = pid;
        _writer = writer;
    }

    internal void SubscribeToEvents(EventPipeEventSource source)
    {
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ExceptionStart += HandleExceptionStartEvent,
            () => source.Clr.ExceptionStart -= HandleExceptionStartEvent
        );
    }

    private void HandleExceptionStartEvent(ExceptionTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Exception Thrown",
            PredefinedProvider.Exceptions,
            evt.TimeStamp,
            $"{evt.ExceptionType}: {evt.ExceptionMessage}"
        );
        _writer.TryWrite(trace);
    }
}