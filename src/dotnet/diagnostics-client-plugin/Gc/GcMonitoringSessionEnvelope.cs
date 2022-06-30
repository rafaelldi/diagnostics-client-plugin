using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.Rd.Tasks;

namespace DiagnosticsClientPlugin.Gc;

internal sealed class GcMonitoringSessionEnvelope
{
    private readonly GcMonitoringHandler _handler;
    private readonly Lifetime _lifetime;
    private readonly GcEventProducer _producer;
    private readonly GcEventProtocolExporter _exporter;

    internal GcMonitoringSessionEnvelope(int pid, GcMonitoringHandler handler, Lifetime lifetime)
    {
        _handler = handler;
        _lifetime = lifetime;
        Session = new GcMonitoringSession(pid);
        
        var channel = Channel.CreateBounded<ValueGcEvent>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        _producer = new GcEventProducer(pid, channel.Writer, _lifetime);
        _exporter = new GcEventProtocolExporter(Session, channel.Reader);

        Session.Monitor.Set(async (lt, duration) => await MonitorAsync(duration, lt));
        Session.Close.Advise(lifetime, _ => Close());
    }
    
    internal GcMonitoringSession Session { get; }

    internal async Task<Unit> MonitorAsync(int? duration, Lifetime lifetime)
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