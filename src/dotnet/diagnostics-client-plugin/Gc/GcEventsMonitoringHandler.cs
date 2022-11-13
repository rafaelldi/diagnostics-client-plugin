using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Gc;

[SolutionComponent]
internal sealed class GcEventsMonitoringHandler
{
    public GcEventsMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.GcEventMonitoringSessions.View(lifetime, (lt, pid, session) => Handle(lt, pid, session));
    }

    private static void Handle(Lifetime lt, int pid, GcEventMonitoringSession session)
    {
        var envelope = new GcEventMonitoringSessionEnvelope(pid, session, lt);
        lt.KeepAlive(envelope);
    }
}