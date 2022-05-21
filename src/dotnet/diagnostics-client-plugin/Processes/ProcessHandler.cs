using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Processes;

[SolutionComponent]
internal sealed class ProcessHandler
{
    private readonly DiagnosticsHostModel _model;

    public ProcessHandler(ISolution solution, Lifetime lifetime)
    {
        _model = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        _model.ProcessList.Refresh.Advise(lifetime, _ => Refresh());
    }

    private void Refresh()
    {
        var processes = DiagnosticsClient.GetPublishedProcesses().ToList();

        var processInfos = new List<ProcessInfo>(processes.Count);
        foreach (var pid in processes)
        {
            try
            {
                var process = Process.GetProcessById(pid);
                var client = new DiagnosticsClient(pid);
                var additionalProcessInfo = client.GetProcessInfo();
                var filename = process.MainModule?.FileName;
                var startTime = process.StartTime.ToString(CultureInfo.CurrentCulture);

                var pi = new ProcessInfo(
                    process.Id, 
                    process.ProcessName, 
                    filename, 
                    startTime,
                    additionalProcessInfo.CommandLine,
                    additionalProcessInfo.OperatingSystem,
                    additionalProcessInfo.ProcessArchitecture);

                processInfos.Add(pi);
            }
            catch (ArgumentException)
            {
                //The identifier might be expired.
            }
        }

        _model.ProcessList.Items.Clear();

        foreach (var process in processInfos.OrderBy(it => it.ProcessId))
        {
            _model.ProcessList.Items.Add(process);
        }
    }
}