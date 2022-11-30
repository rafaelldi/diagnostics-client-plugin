using System.Threading.Channels;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Parsers.Clr;

namespace DiagnosticsClientPlugin.Traces.EventHandlers;

internal sealed class LoaderEventHandler : IEventHandler
{
    private readonly int _pid;
    private readonly ChannelWriter<ValueTrace> _writer;

    internal LoaderEventHandler(int pid, ChannelWriter<ValueTrace> writer)
    {
        _pid = pid;
        _writer = writer;
    }

    public void SubscribeToEvents(EventPipeEventSource source)
    {
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.LoaderAssemblyLoad += HandleLoaderAssemblyLoadEvent,
            () => source.Clr.LoaderAssemblyLoad -= HandleLoaderAssemblyLoadEvent
        );
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.LoaderAssemblyUnload += HandleLoaderAssemblyUnloadEvent,
            () => source.Clr.LoaderAssemblyUnload -= HandleLoaderAssemblyUnloadEvent
        );
    }

    private void HandleLoaderAssemblyLoadEvent(AssemblyLoadUnloadTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Assembly Load",
            PredefinedProvider.Loader,
            evt.TimeStamp,
            $"Assembly name: {evt.FullyQualifiedAssemblyName}"
        );
        _writer.TryWrite(trace);
    }

    private void HandleLoaderAssemblyUnloadEvent(AssemblyLoadUnloadTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Assembly Unload",
            PredefinedProvider.Loader, 
            evt.TimeStamp,
            $"Assembly name: {evt.FullyQualifiedAssemblyName}"
        );
        _writer.TryWrite(trace);
    }
}