using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.Protocol;

namespace DiagnosticsClientPlugin.Traces;

[SolutionComponent]
internal sealed class TraceMonitoringHandler
{
    public TraceMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.LiveTraceSessions.View(lifetime, Handle);
    }

    private static void Handle(Lifetime lt, int pid, LiveTraceSession session)
    {
        var envelope = new TraceMonitoringSessionEnvelope(pid, session, lt);
        lt.KeepAlive(envelope);
    }
}