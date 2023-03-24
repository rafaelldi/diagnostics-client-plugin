using System.Collections.Generic;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Traces.EventHandlers;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProducer
{
    private readonly EventPipeSessionManager _sessionManager;
    private readonly TraceProducerConfiguration _configuration;
    private readonly List<IEventHandler> _handlers;

    public TraceProducer(
        int pid,
        TraceProducerConfiguration configuration,
        ChannelWriter<ValueTrace> writer,
        Lifetime lifetime)
    {
        _sessionManager = new EventPipeSessionManager(pid);
        _configuration = configuration;

        _handlers = new List<IEventHandler>(8);

        if (_configuration.IsHttpEnabled)
        {
            _handlers.Add(new HttpEventHandler(pid, writer));
        }

        if (_configuration.IsAspNetEnabled)
        {
            _handlers.Add(new AspNetEventHandler(pid, writer));
        }

        if (_configuration.IsEfEnabled)
        {
            _handlers.Add(new EfEventHandler(pid, writer));
        }

        if (_configuration.IsExceptionsEnabled)
        {
            _handlers.Add(new ExceptionEventHandler(pid, writer));
        }

        if (_configuration.IsThreadsEnabled)
        {
            _handlers.Add(new ThreadEventHandler(pid, writer));
        }

        if (_configuration.IsContentionsEnabled)
        {
            _handlers.Add(new ContentionEventHandler(pid, writer));
        }

        if (_configuration.IsTasksEnabled)
        {
            _handlers.Add(new TaskEventHandler(pid, writer));
        }

        if (_configuration.IsLoaderEnabled)
        {
            _handlers.Add(new LoaderEventHandler(pid, writer));
        }

        lifetime.OnTermination(() => writer.Complete());
    }

    internal Task Produce()
    {
        var session = _sessionManager.StartSession(_configuration.EventPipeProviders, false);
        Lifetime.AsyncLocal.Value.AddDispose(session);

        var source = new EventPipeEventSource(session.EventStream);
        Lifetime.AsyncLocal.Value.AddDispose(source);

        SubscribeToEvents(source);

        var cancellationToken = Lifetime.AsyncLocal.Value.ToCancellationToken();
        cancellationToken.Register(() => EventPipeSessionManager.StopSession(session));

        return Task.Run(() => source.Process(), Lifetime.AsyncLocal.Value);
    }

    private void SubscribeToEvents(EventPipeEventSource source)
    {
        foreach (var handler in _handlers)
        {
            handler.SubscribeToEvents(source);
        }
    }
}