using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.EventPipes;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;
using Microsoft.Diagnostics.Tracing;

namespace DiagnosticsClientPlugin.Gc;

[SolutionComponent]
internal sealed class TriggerGcCollectionHandler
{
    public TriggerGcCollectionHandler(ISolution solution)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.TriggerGc.Set(async (lt, command) => await TriggerGcCollection(command, lt));
    }

    private async Task<Unit> TriggerGcCollection(TriggerGcCommand command, Lifetime lifetime)
    {
        var providers = new[] { EventPipeProviderFactory.CreateGcHeapCollect() };
        var sessionManager = new EventPipeSessionManager(command.Pid);
        using var session = sessionManager.StartSession(providers);
        var source = new EventPipeEventSource(session.EventStream);

        var processTask = Task.Run(() => source.Process(), lifetime);

        EventPipeSessionManager.StopSession(session);

        await processTask;

        return Unit.Instance;
    }
}