using System;
using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;
using Microsoft.Diagnostics.NETCore.Client;
using Microsoft.Diagnostics.Symbols;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Etlx;
using Microsoft.Diagnostics.Tracing.Stacks;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.StackTrace;

[SolutionComponent]
internal sealed class StackTraceCollectionHandler
{
    public StackTraceCollectionHandler(ISolution solution)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.CollectStackTrace.Set(async (lt, command) => await Collect(command));
    }

    private async Task<string> Collect(CollectStackTraceCommand command)
    {
        var sessionFilename = $"{Path.GetRandomFileName()}.nettrace";
        var sessionFilePath = Path.Combine(Path.GetTempPath(), sessionFilename);

        await CollectTraces(command, sessionFilePath);
        return ParseSessionFile(sessionFilePath);
    }

    private async Task CollectTraces(CollectStackTraceCommand command, string sessionFilePath)
    {
        var client = new DiagnosticsClient(command.Pid);
        var providers = new EventPipeProvider[]
        {
            new(SampleProfilerProvider, EventLevel.Informational)
        };
        using var session = client.StartEventPipeSession(providers);
        using var fileStream = new FileStream(sessionFilePath, FileMode.Create, FileAccess.Write);

        var copyTask = session.EventStream.CopyToAsync(fileStream);
        await Task.Delay(TimeSpan.FromMilliseconds(10));
        session.Stop();
        await copyTask;
    }

    private string ParseSessionFile(string sessionFilePath)
    {
        var traceLogFilePath = TraceLog.CreateFromEventPipeDataFile(sessionFilePath);
        using var symbolReader = new SymbolReader(TextWriter.Null)
            { SymbolPath = SymbolPath.MicrosoftSymbolServerPath };
        using var traceLog = new TraceLog(traceLogFilePath);
        var stackSource = new MutableTraceEventStackSource(traceLog)
        {
            OnlyManagedCodeStacks = true
        };

        var computer = new SampleProfilerThreadTimeComputer(traceLog, symbolReader);
        computer.GenerateThreadTimeStacks(stackSource);

        var samplesByThread = new Dictionary<int, StackSourceSample>();
        stackSource.ForEach(it =>
        {
            var stackIndex = it.StackIndex;
            var frameName = stackSource.GetFrameName(stackSource.GetFrameIndex(stackIndex), false);
            while (!frameName.StartsWith("Thread"))
            {
                stackIndex = stackSource.GetCallerIndex(stackIndex);
                frameName = stackSource.GetFrameName(stackSource.GetFrameIndex(stackIndex), false);
            }

            var threadId = int.Parse(frameName[8..^1]);
            if (!samplesByThread.ContainsKey(threadId))
            {
                samplesByThread[threadId] = it;
            }
        });

        var stackTraces = SerializeStackTraces(samplesByThread, stackSource);

        return stackTraces;
    }

    private string SerializeStackTraces(Dictionary<int, StackSourceSample> samplesByThread,
        MutableTraceEventStackSource stackSource)
    {
        var sb = new StringBuilder();

        foreach (var threadSamples in samplesByThread)
        {
            sb.AppendLine($"Thread (0x{threadSamples.Key:X}):");

            var stackIndex = threadSamples.Value.StackIndex;
            var frameName = stackSource.GetFrameName(stackSource.GetFrameIndex(stackIndex), false);
            while (!frameName.StartsWith("Thread"))
            {
                sb.AppendLine($"  {frameName}".Replace("UNMANAGED_CODE_TIME", "[Native Frames]"));
                stackIndex = stackSource.GetCallerIndex(stackIndex);
                frameName = stackSource.GetFrameName(stackSource.GetFrameIndex(stackIndex), false);
            }

            sb.AppendLine();
        }

        return sb.ToString();
    }
}