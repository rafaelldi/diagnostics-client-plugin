using System.Threading.Tasks;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;
using Microsoft.Diagnostics.Tracing;

namespace DiagnosticsClientPlugin.Gc;

[SolutionComponent]
internal sealed class TriggerGcCollectionHandler
{
    public TriggerGcCollectionHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.TriggerGc.Advise(lifetime, Handle);
    }

    private static void Handle(int pid)
    {
        var providers = new[] { EventPipeProviderFactory.CreateGcHeapCollect() };
        var sessionManager = new EventPipeSessionManager(pid);
        using var session = sessionManager.StartSession(providers);
        var source = new EventPipeEventSource(session.EventStream);

        Task.Run(() => source.Process());

        EventPipeSessionManager.StopSession(session);
    }
}