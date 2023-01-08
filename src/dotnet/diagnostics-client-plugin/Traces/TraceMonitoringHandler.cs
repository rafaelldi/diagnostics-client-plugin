using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Traces;

[SolutionComponent]
internal sealed class TraceMonitoringHandler
{
    public TraceMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.LiveTraceSessions.View(lifetime, (lt, pid, session) => Handle(lt, pid, session));
    }

    private static void Handle(Lifetime lt, int pid, LiveTraceSession session)
    {
        var envelope = new TraceMonitoringSessionEnvelope(pid, session, lt);
        lt.KeepAlive(envelope);
    }
}