using System.IO;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Traces;

[SolutionComponent]
internal sealed class TraceCollectionHandler
{
    public TraceCollectionHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.PersistentTraceSessions.View(lifetime, (lt, pid, session) => Handle(lt, pid, session));
    }

    private static void Handle(Lifetime lt, int pid, PersistentTraceSession session)
    {
        var providers = new TraceProviderCollection(session.Providers, session.Profile, session.PredefinedProviders);
        var sessionManager = new EventPipeSessionManager(pid);
        var eventPipeSession = sessionManager.StartSession(providers.EventPipeProviders);
        lt.AddDispose(eventPipeSession);
        
        var fileStream = new FileStream(session.FilePath, FileMode.Create, FileAccess.Write);
        lt.AddDispose(fileStream);

        var cancellationToken = lt.ToCancellationToken();
        cancellationToken.Register(() => EventPipeSessionManager.StopSession(eventPipeSession));

        var copyTask = eventPipeSession.EventStream.CopyToAsync(fileStream, 81920);
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await copyTask);
    }
}