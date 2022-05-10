using System;
using System.IO;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;
using JetBrains.Util;

namespace DiagnosticsClientPlugin.Traces;

[SolutionComponent]
internal sealed class TraceCollectionHandler
{
    private readonly DiagnosticsHostModel _hostModel;

    public TraceCollectionHandler(ISolution solution)
    {
        _hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        _hostModel.CollectTraces.Set(async (lt, command) => await Collect(command, lt));
    }

    private async Task<Unit> Collect(CollectTracesCommand command, Lifetime lifetime)
    {
        _hostModel.TraceCollectionSessions.Add(lifetime, command.Pid);

        var providers = new TraceProviderCollection(command.Providers, command.Profile);
        var sessionManager = new EventPipeSessionManager(command.Pid);
        using var session = sessionManager.StartSession(providers.EventPipeProviders, true);

        using var fileStream = new FileStream(command.FilePath, FileMode.Create, FileAccess.Write);

        var copyTask = session.EventStream.CopyToAsync(fileStream, 81920);

        var duration = command.Duration.HasValue
            ? TimeSpan.FromSeconds(command.Duration.Value)
            : TimeSpan.FromMilliseconds(-1);
        try
        {
            await Task.Delay(duration, lifetime);
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }

        EventPipeSessionManager.StopSession(session);

        await copyTask;

        return Unit.Instance;
    }
}