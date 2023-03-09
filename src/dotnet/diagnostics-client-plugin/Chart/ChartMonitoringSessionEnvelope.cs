using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters;
using DiagnosticsClientPlugin.Counters.Producer;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Chart;

internal sealed class ChartMonitoringSessionEnvelope
{
    private readonly ChartProtocolExporter _exporter;
    private readonly CounterProducer _producer;

    internal ChartMonitoringSessionEnvelope(int pid, LiveChartSession session, Lifetime lifetime)
    {
        var channel = Channel.CreateBounded<ValueCounter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _exporter = new ChartProtocolExporter(session, channel.Reader);
        var configuration = new CounterProducerConfiguration(
            Guid.NewGuid().ToString(),
            "System.Runtime[cpu-usage]",
            null,
            1,
            1000,
            10
        );
        _producer = new CounterProducer(pid, configuration, channel.Writer, lifetime);

        session.Active.WhenTrue(lifetime, lt => Handle(lt));
    }

    private void Handle(Lifetime lt)
    {
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _exporter.ConsumeAsync());
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _producer.Produce());
    }
}