using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceMonitoringSessionEnvelope
{
    private readonly TraceProtocolExporter _exporter;
    private readonly TraceProducer _producer;

    internal TraceMonitoringSessionEnvelope(int pid, TraceMonitoringSession session, Lifetime lifetime)
    {
        var channel = Channel.CreateBounded<ValueTrace>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _exporter = new TraceProtocolExporter(session, channel.Reader);
        var traceProducerConfiguration = new TraceProducerConfiguration(session.PredefinedProviders);
        _producer = new TraceProducer(pid, traceProducerConfiguration, channel.Writer, lifetime);

        session.Active.WhenTrue(lifetime, lt => Handle(lt));
    }

    private void Handle(Lifetime lt)
    {
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _exporter.ConsumeAsync());
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _producer.Produce());
    }
}