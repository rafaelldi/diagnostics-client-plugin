using System.Collections.Concurrent;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Gc;

[SolutionComponent]
internal sealed class GcMonitoringHandler
{
    private readonly Lifetime _lifetime;
    private readonly DiagnosticsHostModel _hostModel;

    private readonly ConcurrentDictionary<int, (GcMonitoringSessionEnvelope Envelope, LifetimeDefinition Definition)>
        _sessions = new();

    public GcMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        _lifetime = lifetime;
        _hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();

        _hostModel.MonitorGc.Set(async (lt, command) => await MonitorAsync(command, lt));
    }

    private async Task<Unit> MonitorAsync(MonitorGcCommand command, Lifetime lifetime)
    {
        if (_sessions.TryGetValue(command.Pid, out var session))
        {
            await session.Envelope.MonitorAsync(command.Duration, lifetime);
        }
        else
        {
            var definition = _lifetime.CreateNested();
            var envelope = new GcMonitoringSessionEnvelope(command.Pid, this, definition.Lifetime);
            if (!_sessions.TryAdd(command.Pid, (envelope, definition)))
            {
                return Unit.Instance;
            }

            _hostModel.GcMonitoringSessions.Add(definition.Lifetime, command.Pid, envelope.Session);

            await envelope.MonitorAsync(command.Duration, lifetime);
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