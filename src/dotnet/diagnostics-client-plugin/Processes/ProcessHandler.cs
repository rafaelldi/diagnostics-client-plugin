using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
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
        _model.ProcessList.Active.WhenTrue(lifetime, lt => Handle(lt));
    }

    private void Handle(Lifetime lt)
    {
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await RefreshAsync());
    }

    private async Task RefreshAsync()
    {
        try
        {
            while (true)
            {
                RefreshProcessList();
                await Task.Delay(TimeSpan.FromSeconds(3), Lifetime.AsyncLocal.Value);
            }
        }
        catch (OperationCanceledException)
        {
            _model.ProcessList.Items.Clear();
        }
    }

    private void RefreshProcessList()
    {
        var processes = DiagnosticsClient.GetPublishedProcesses().ToList();

        var existingProcesses = _model.ProcessList.Items.Keys.ToArray();
        var newProcesses = new Dictionary<int, ProcessInfo>(processes.Count);
        foreach (var pid in processes)
        {
            if (existingProcesses.Contains(pid))
            {
                continue;
            }

            try
            {
                var process = Process.GetProcessById(pid);
                var client = new DiagnosticsClient(pid);
                var additionalProcessInfo = client.GetProcessInfo();
                var filename = process.MainModule?.FileName;
                var startTime = process.StartTime.ToString(CultureInfo.CurrentCulture);
                var environment = client
                                      .GetProcessEnvironment()
                                      .Select(it => new ProcessEnvironmentVariable(it.Key, it.Value))
                                      .ToArray();

                var processInfo = new ProcessInfo(
                    process.ProcessName,
                    filename,
                    startTime,
                    additionalProcessInfo.CommandLine,
                    additionalProcessInfo.OperatingSystem,
                    additionalProcessInfo.ProcessArchitecture,
                    environment
                );

                newProcesses[pid] = processInfo;
            }
            catch (ArgumentException)
            {
                //The identifier might be expired.
            }
        }

        foreach (var createdProcess in newProcesses)
        {
            _model.ProcessList.Items.Add(createdProcess);
        }

        foreach (var removedProcess in existingProcesses.Except(processes))
        {
            _model.ProcessList.Items.Remove(removedProcess);
        }
    }
}