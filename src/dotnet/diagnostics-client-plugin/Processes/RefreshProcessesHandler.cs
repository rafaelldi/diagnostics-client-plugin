using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Processes;

[SolutionComponent]
internal sealed class RefreshProcessesHandler
{
    private readonly DiagnosticsHostModel _model;

    public RefreshProcessesHandler(ISolution solution, Lifetime lifetime)
    {
        _model = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        _model.ProcessList.Refresh.Advise(lifetime, _ => Handle());
    }

    private void Handle()
    {
        var processes = DiagnosticsClient.GetPublishedProcesses().ToList();

        var processInfos = new List<ProcessInfo>(processes.Count);
        foreach (var pid in processes)
        {
            var process = Process.GetProcessById(pid);
            var filename = process.MainModule?.FileName;
            var startTime = process.StartTime.ToString(CultureInfo.CurrentCulture);
            var pi = new ProcessInfo(process.Id, process.ProcessName, filename, startTime);
            processInfos.Add(pi);
        }

        _model.ProcessList.Items.Clear();

        foreach (var process in processInfos.OrderBy(it => it.ProcessId))
        {
            _model.ProcessList.Items.Add(process);
        }
    }
}