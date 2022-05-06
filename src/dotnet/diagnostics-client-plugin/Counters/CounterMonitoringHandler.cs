using System.Collections.Concurrent;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Producer;
using DiagnosticsClientPlugin.Generated;
using JetBrains;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Counters.Monitoring;

[SolutionComponent]
internal sealed class CounterMonitoringHandler
{
    private readonly Lifetime _lifetime;
    private readonly DiagnosticsHostModel _hostModel;

    private readonly ConcurrentDictionary<int, (MonitoringSessionEnvelope Envelope, LifetimeDefinition Definition)>
        _sessions = new();

    public CounterMonitoringHandler(ISolution solution, Lifetime lifetime)
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
            var providers = new CounterProviderCollection(command.Providers, command.RefreshInterval);
            var configuration = new CountersProducerConfiguration(command.RefreshInterval, providers);
            var envelope = new MonitoringSessionEnvelope(command.Pid, configuration, this, definition.Lifetime);
            if (!_sessions.TryAdd(command.Pid, (envelope, definition)))
            {
                return Unit.Instance;
            }

            _hostModel.CounterMonitoringSessions.Add(definition.Lifetime, command.Pid, envelope.Session);

            await envelope.Monitor(command.Duration, lifetime);
        }

        return Unit.Instance;
    }

    internal void CloseSession(int pid)
    {
        if (_sessions.TryRemove(pid, out var session))
        {
            session.Definition.Terminate();
        }
    }
}