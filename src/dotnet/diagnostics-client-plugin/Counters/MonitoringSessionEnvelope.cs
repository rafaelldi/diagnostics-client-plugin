using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Consuming;
using DiagnosticsClientPlugin.Counters.Producing;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.Rd.Tasks;
using Counter = DiagnosticsClientPlugin.Counters.Common.Counter;

namespace DiagnosticsClientPlugin.Counters.Monitoring;

internal sealed class MonitoringSessionEnvelope
{
    private readonly SequentialLifetimes _sessionLifetimes;
    private readonly ExportToProtocolCountersConsumer _consumer;
    private readonly CountersProducer _producer;

    internal MonitoringSessionEnvelope(int pid, CountersProducerConfiguration producerConfiguration, Lifetime lifetime)
    {
        Session = new CountersMonitoringSession(pid);
        _sessionLifetimes = new SequentialLifetimes(lifetime);

        var channel = Channel.CreateBounded<Counter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _consumer = new ExportToProtocolCountersConsumer(Session, channel.Reader);
        _producer = new CountersProducer(pid, producerConfiguration, channel.Writer, lifetime);

        Session.Monitor.Set(async (lt, duration) => await Monitor(duration, lt));
        Session.Stop.Advise(lifetime, _ => Stop());
    }

    internal CountersMonitoringSession Session { get; }

    internal async Task<Unit> Monitor(int? duration, Lifetime lifetime)
    {
        if (!_sessionLifetimes.IsCurrentTerminated)
        {
            return Unit.Instance;
        }

        var sessionLifetime = CreateSessionLifetime(_sessionLifetimes.Next(), lifetime, duration);
        sessionLifetime.OnTermination(() => _sessionLifetimes.TerminateCurrent());

        sessionLifetime.Bracket(
            () => Session.Active.Value = true,
            () => Session.Active.Value = false
        );

        sessionLifetime.Bracket(
            () => Session.Started(),
            () => Session.Stopped()
        );

        var consumerTask = _consumer.ConsumeAsync(sessionLifetime);
        var producerTask = _producer.Produce(sessionLifetime);

        var completedTask = await Task.WhenAny(consumerTask, producerTask);
        await completedTask;

        return Unit.Instance;
    }

    private Lifetime CreateSessionLifetime(
        in Lifetime nextLifetime,
        in Lifetime operationLifetime,
        int? duration)
    {
        var sessionLifetime = nextLifetime.Intersect(operationLifetime);
        return duration.HasValue
            ? sessionLifetime.CreateTerminatedAfter(TimeSpan.FromSeconds(duration.Value))
            : sessionLifetime;
    }

    internal void Stop()
    {
        _sessionLifetimes.TerminateCurrent();
    }
}