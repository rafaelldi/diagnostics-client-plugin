using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Counters;

[SolutionComponent]
internal sealed class CounterMonitoringHandler
{
    public CounterMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.LiveCounterSessions.View(lifetime, (lt, pid, session) => Handle(lt, pid, session));
    }

    private static void Handle(Lifetime lt, int pid, LiveCounterSession session)
    {
        var envelope = new CounterMonitoringSessionEnvelope(pid, session, lt);
        lt.KeepAlive(envelope);
    }
}