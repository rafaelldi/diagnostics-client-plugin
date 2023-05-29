using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.Protocol;

namespace DiagnosticsClientPlugin.Chart;

[SolutionComponent]
internal sealed class ChartMonitoringHandler
{
    public ChartMonitoringHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.LiveChartSessions.View(lifetime, Handle);
    }

    private static void Handle(Lifetime lt, int pid, LiveChartSession session)
    {
        var envelope = new ChartMonitoringSessionEnvelope(pid, session, lt);
        lt.KeepAlive(envelope);
    }
}