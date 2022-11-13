using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Gc;

internal sealed class GcEventMonitoringSessionEnvelope
{
    private readonly GcEventProducer _producer;
    private readonly GcEventProtocolExporter _exporter;

    internal GcEventMonitoringSessionEnvelope(int pid, GcEventMonitoringSession session, Lifetime lifetime)
    {
        var channel = Channel.CreateBounded<ValueGcEvent>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _exporter = new GcEventProtocolExporter(session, channel.Reader);
        _producer = new GcEventProducer(pid, channel.Writer, lifetime);

        session.Active.WhenTrue(lifetime, lt => Handle(lt));
    }

    private void Handle(Lifetime lt)
    {
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _exporter.ConsumeAsync());
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await _producer.Produce());
    }
}