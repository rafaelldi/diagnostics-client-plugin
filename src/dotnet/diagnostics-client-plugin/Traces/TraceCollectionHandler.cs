using System;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;

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
        throw new NotImplementedException();
    }
}