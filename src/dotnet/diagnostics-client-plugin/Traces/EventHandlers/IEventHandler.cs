using Microsoft.Diagnostics.Tracing;

namespace DiagnosticsClientPlugin.Traces.EventHandlers;

internal interface IEventHandler
{
    void SubscribeToEvents(EventPipeEventSource source);
}