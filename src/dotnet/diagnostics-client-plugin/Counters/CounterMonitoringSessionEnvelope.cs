using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Exporters;
using DiagnosticsClientPlugin.Counters.Producer;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Counters;

internal sealed class CounterMonitoringSessionEnvelope
{
    private readonly CounterProtocolExporter _exporter;
    private readonly CounterProducer _producer;

    internal CounterMonitoringSessionEnvelope(int pid, CountersMonitoringSession session, Lifetime lifetime)
    {
        var channel = Channel.CreateBounded<ValueCounter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _exporter = CreateExporter(session, channel);
        _producer = CreateProducer(pid, session, channel, lifetime);

        session.Active.WhenTrue(lifetime, lt => Handle(lt));
    }

    private void Handle(Lifetime lt)
    {
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _exporter.ConsumeAsync());
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _producer.Produce());
    }

    private static CounterProtocolExporter CreateExporter(CountersMonitoringSession session,
        Channel<ValueCounter> channel) => new(session, channel.Reader);

    private static CounterProducer CreateProducer(
        int pid,
        CountersMonitoringSession session,
        Channel<ValueCounter> channel,
        Lifetime lt)
    {
        var configuration = new CounterProducerConfiguration(
            Guid.NewGuid().ToString(),
            session.Providers,
            session.Metrics,
            session.RefreshInterval,
            session.MaxTimeSeries,
            session.MaxHistograms
        );
        return new CounterProducer(pid, configuration, channel.Writer, lt);
    }
}