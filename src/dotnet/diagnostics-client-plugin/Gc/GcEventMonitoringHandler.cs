using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Gc;

[SolutionComponent]
internal sealed class GcEventMonitoringHandler
{
    public GcEventMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.LiveGcEventSessions.View(lifetime, Handle);
    }

    private static void Handle(Lifetime lt, int pid, LiveGcEventSession session)
    {
        var envelope = new GcEventMonitoringSessionEnvelope(pid, session, lt);
        lt.KeepAlive(envelope);
    }
}