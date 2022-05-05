using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Counters.Exporters;
using DiagnosticsClientPlugin.Counters.Producer;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.Rd.Tasks;

namespace DiagnosticsClientPlugin.Counters.Monitoring;

internal sealed class MonitoringSessionEnvelope
{
    private readonly CounterMonitoringHandler _handler;
    private readonly Lifetime _lifetime;
    private readonly ProtocolCounterExporter _exporter;
    private readonly CountersProducer _producer;

    internal MonitoringSessionEnvelope(int pid, CountersProducerConfiguration producerConfiguration,
        CounterMonitoringHandler handler, Lifetime lifetime)
    {
        _handler = handler;
        _lifetime = lifetime;
        Session = new CountersMonitoringSession(pid);

        var channel = Channel.CreateBounded<ValueCounter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _exporter = new ProtocolCounterExporter(Session, channel.Reader);
        _producer = new CountersProducer(pid, producerConfiguration, channel.Writer, lifetime);

        Session.Monitor.Set(async (lt, duration) => await Monitor(duration, lt));
        Session.Close.Advise(lifetime, _ => Close());
    }

    internal CountersMonitoringSession Session { get; }

    internal async Task<Unit> Monitor(int? duration, Lifetime lifetime)
    {
        var operationLifetime = _lifetime.IntersectWithTimer(lifetime, duration);

        operationLifetime.Bracket(
            () => Session.Active.Value = true,
            () => Session.Active.Value = false
        );

        var exporterTask = _exporter.ConsumeAsync(operationLifetime);
        var producerTask = _producer.Produce(operationLifetime);

        var completedTask = await Task.WhenAny(exporterTask, producerTask);
        await completedTask;

        return Unit.Instance;
    }

    private void Close()
    {
        _handler.CloseSession(Session.Pid);
    }
}