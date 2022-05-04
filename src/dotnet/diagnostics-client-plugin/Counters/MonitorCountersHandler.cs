using System.Collections.Concurrent;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Producing;
using DiagnosticsClientPlugin.Generated;
using JetBrains;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Counters.Monitoring;

[SolutionComponent]
internal sealed class MonitorCountersHandler
{
    private readonly Lifetime _lifetime;
    private readonly DiagnosticsHostModel _hostModel;

    private readonly ConcurrentDictionary<int, (MonitoringSessionEnvelope Envelope, LifetimeDefinition Definition)>
        _sessions = new();

    public MonitorCountersHandler(ISolution solution, Lifetime lifetime)
    {
        _lifetime = lifetime;
        _hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();

        _hostModel.MonitorCounters.Set(async (lt, command) => await Monitor(command, lt));
    }

    private async Task<Unit> Monitor(MonitorCountersCommand command, Lifetime lifetime)
    {
        if (_sessions.TryGetValue(command.Pid, out var session))
        {
            await session.Envelope.Monitor(command.Duration, lifetime);
        }
        else
        {
            var definition = _lifetime.CreateNested();
            var providers = new CounterProviderCollection(command.Providers);
            var configuration = new CountersProducerConfiguration(command.RefreshInterval, providers);
            var envelope = new MonitoringSessionEnvelope(command.Pid, configuration, definition.Lifetime);
            if (!_sessions.TryAdd(command.Pid, (envelope, definition)))
            {
                return Unit.Instance;
            }

            _hostModel.CountersMonitoringSessions.Add(definition.Lifetime, command.Pid, envelope.Session);

            await envelope.Monitor(command.Duration, lifetime);
        }

        return Unit.Instance;
    }

    private void Close(int pid)
    {
        if (_sessions.TryRemove(pid, out var session))
        {
            session.Definition.Terminate();
        }
    }
}