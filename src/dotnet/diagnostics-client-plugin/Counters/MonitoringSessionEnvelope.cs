using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Counters.Consuming;
using DiagnosticsClientPlugin.Counters.Producing;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.Rd.Tasks;

namespace DiagnosticsClientPlugin.Counters.Monitoring;

internal sealed class MonitoringSessionEnvelope
{
    private readonly Lifetime _lifetime;
    private readonly ExportToProtocolCountersConsumer _consumer;
    private readonly CountersProducer _producer;

    internal MonitoringSessionEnvelope(int pid, CountersProducerConfiguration producerConfiguration, Lifetime lifetime)
    {
        _lifetime = lifetime;
        Session = new CountersMonitoringSession(pid);

        var channel = Channel.CreateBounded<ValueCounter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _consumer = new ExportToProtocolCountersConsumer(Session, channel.Reader);
        _producer = new CountersProducer(pid, producerConfiguration, channel.Writer, lifetime);

        Session.Monitor.Set(async (lt, duration) => await Monitor(duration, lt));
        Session.Close.Advise(lifetime, _ => Close());
    }

    internal CountersMonitoringSession Session { get; }

    internal async Task<Unit> Monitor(int? duration, Lifetime lifetime)
    {
        var operationLifetime = CreateOperationLifetime(lifetime, duration);

        operationLifetime.Bracket(
            () => Session.Active.Value = true,
            () => Session.Active.Value = false
        );

        var consumerTask = _consumer.ConsumeAsync(operationLifetime);
        var producerTask = _producer.Produce(operationLifetime);

        var completedTask = await Task.WhenAny(consumerTask, producerTask);
        await completedTask;

        return Unit.Instance;
    }

    private Lifetime CreateOperationLifetime(in Lifetime operationLifetime, int? duration)
    {
        var sessionLifetime = _lifetime.Intersect(operationLifetime);
        return duration.HasValue
            ? sessionLifetime.CreateTerminatedAfter(TimeSpan.FromSeconds(duration.Value))
            : sessionLifetime;
    }

    private void Close()
    {
        throw new NotImplementedException();
    }
}