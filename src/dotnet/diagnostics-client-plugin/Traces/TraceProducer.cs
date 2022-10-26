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

    private readonly HttpEventHandler _httpEventHandler;
    private readonly AspNetEventHandler _aspNetEventHandler;
    private readonly EfEventHandler _efEventHandler;
    private readonly ExceptionEventHandler _exceptionEventHandler;
    private readonly ThreadEventHandler _threadEventHandler;
    private readonly ContentionEventHandler _contentionEventHandler;
    private readonly TaskEventHandler _taskEventHandler;
    private readonly LoaderEventHandler _loaderEventHandler;

    public TraceProducer(
        int pid,
        TraceProducerConfiguration configuration,
        ChannelWriter<ValueTrace> writer,
        Lifetime lifetime)
    {
        _sessionManager = new EventPipeSessionManager(pid);
        _configuration = configuration;

        _httpEventHandler = new HttpEventHandler(pid, writer);
        _aspNetEventHandler = new AspNetEventHandler(pid, writer);
        _efEventHandler = new EfEventHandler(pid, writer);
        _exceptionEventHandler = new ExceptionEventHandler(pid, writer);
        _threadEventHandler = new ThreadEventHandler(pid, writer);
        _contentionEventHandler = new ContentionEventHandler(pid, writer);
        _taskEventHandler = new TaskEventHandler(pid, writer);
        _loaderEventHandler = new LoaderEventHandler(pid, writer);

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
        if (_configuration.IsHttpEnabled)
        {
            _httpEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsAspNetEnabled)
        {
            _aspNetEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsEfEnabled)
        {
            _efEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsExceptionsEnabled)
        {
            _exceptionEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsThreadsEnabled)
        {
            _threadEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsContentionsEnabled)
        {
            _contentionEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsTasksEnabled)
        {
            _taskEventHandler.SubscribeToEvents(source);
        }

        if (_configuration.IsLoaderEnabled)
        {
            _loaderEventHandler.SubscribeToEvents(source);
        }
    }
}