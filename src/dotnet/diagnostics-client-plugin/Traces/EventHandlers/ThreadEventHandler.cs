using System.Threading.Channels;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Parsers.Clr;

namespace DiagnosticsClientPlugin.Traces.EventHandlers;

internal sealed class ThreadEventHandler : IEventHandler
{
    private readonly int _pid;
    private readonly ChannelWriter<ValueTrace> _writer;

    internal ThreadEventHandler(int pid, ChannelWriter<ValueTrace> writer)
    {
        _pid = pid;
        _writer = writer;
    }

    public void SubscribeToEvents(EventPipeEventSource source)
    {
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ThreadCreating += HandleThreadCreatingEvent,
            () => source.Clr.ThreadCreating -= HandleThreadCreatingEvent
        );
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ThreadRunning += HandleThreadRunningEvent,
            () => source.Clr.ThreadRunning -= HandleThreadRunningEvent
        );
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ThreadPoolWorkerThreadStart += HandleThreadPoolWorkerThreadStartEvent,
            () => source.Clr.ThreadPoolWorkerThreadStart -= HandleThreadPoolWorkerThreadStartEvent
        );
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ThreadPoolWorkerThreadStop += HandleThreadPoolWorkerThreadStopEvent,
            () => source.Clr.ThreadPoolWorkerThreadStop -= HandleThreadPoolWorkerThreadStopEvent
        );
        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Clr.ThreadPoolWorkerThreadWait += HandleThreadPoolWorkerThreadWaitEvent,
            () => source.Clr.ThreadPoolWorkerThreadWait -= HandleThreadPoolWorkerThreadWaitEvent
        );
    }

    private void HandleThreadCreatingEvent(ThreadStartWorkTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Thread Created",
            PredefinedProvider.Threads,
            evt.TimeStamp,
            $"Thread ID: {evt.ThreadStartWorkID}"
        );
        _writer.TryWrite(trace);
    }

    private void HandleThreadRunningEvent(ThreadStartWorkTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Thread Started",
            PredefinedProvider.Threads, 
            evt.TimeStamp,
            $"Thread ID: {evt.ThreadStartWorkID}"
        );
        _writer.TryWrite(trace);
    }

    private void HandleThreadPoolWorkerThreadStartEvent(ThreadPoolWorkerThreadTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Thread Pool Worker Thread Started",
            PredefinedProvider.Threads,
            evt.TimeStamp,
            $"Active worker threads count: {evt.ActiveWorkerThreadCount}, retired worker thread count: {evt.RetiredWorkerThreadCount}"
        );
        _writer.TryWrite(trace);
    }

    private void HandleThreadPoolWorkerThreadStopEvent(ThreadPoolWorkerThreadTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Thread Pool Worker Thread Stopped",
            PredefinedProvider.Threads,
            evt.TimeStamp,
            $"Active worker threads count: {evt.ActiveWorkerThreadCount}, retired worker thread count: {evt.RetiredWorkerThreadCount}"
        );
        _writer.TryWrite(trace);
    }

    private void HandleThreadPoolWorkerThreadWaitEvent(ThreadPoolWorkerThreadTraceData evt)
    {
        if (evt.ProcessID != _pid) return;
        var trace = new ValueTrace(
            "Thread Pool Worker Thread Starts Waiting",
            PredefinedProvider.Threads,
            evt.TimeStamp,
            $"Active worker threads count: {evt.ActiveWorkerThreadCount}, retired worker thread count: {evt.RetiredWorkerThreadCount}"
        );
        _writer.TryWrite(trace);
    }
}