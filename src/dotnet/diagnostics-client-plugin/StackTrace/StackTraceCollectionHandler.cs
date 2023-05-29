using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.ReSharper.Feature.Services.Protocol;
using Microsoft.Diagnostics.Symbols;
using Microsoft.Diagnostics.Tracing;
using Microsoft.Diagnostics.Tracing.Etlx;
using Microsoft.Diagnostics.Tracing.Stacks;

namespace DiagnosticsClientPlugin.StackTrace;

[SolutionComponent]
internal sealed class StackTraceCollectionHandler
{
    public StackTraceCollectionHandler(ISolution solution)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.CollectStackTrace.Set(async (lt, command) => await CollectAsync(command, lt));
    }

    private static async Task<string> CollectAsync(CollectStackTraceCommand command, Lifetime lifetime)
    {
        var sessionFilename = $"{Path.GetRandomFileName()}.nettrace";
        var sessionFilePath = Path.Combine(Path.GetTempPath(), sessionFilename);
        lifetime.OnTermination(() =>
        {
            if (File.Exists(sessionFilePath))
            {
                File.Delete(sessionFilePath);
            }
        });
        await CollectTracesAsync(command, sessionFilePath, lifetime);

        var traceLogFilePath = TraceLog.CreateFromEventPipeDataFile(sessionFilePath);
        lifetime.OnTermination(() =>
        {
            if (File.Exists(traceLogFilePath))
            {
                File.Delete(traceLogFilePath);
            }
        });
        var stackTraces = ParseSessionFile(traceLogFilePath);
        return stackTraces;
    }

    private static async Task CollectTracesAsync(CollectStackTraceCommand command, string sessionFilePath, Lifetime lifetime)
    {
        var providers = new[] { EventPipeProviderFactory.CreateSampleProvider() };
        var sessionManager = new EventPipeSessionManager(command.Pid);
        using var session = sessionManager.StartSession(providers);

        using var fileStream = new FileStream(sessionFilePath, FileMode.Create, FileAccess.Write);

        // ReSharper disable once MethodSupportsCancellation
        var copyTask = session.EventStream.CopyToAsync(fileStream, 81920);

        try
        {
            await Task.Delay(TimeSpan.FromMilliseconds(10), lifetime);
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }

        EventPipeSessionManager.StopSession(session);

        await copyTask;
    }

    private static string ParseSessionFile(string traceLogFilePath)
    {
        using var symbolReader = new SymbolReader(TextWriter.Null)
        {
            SymbolPath = SymbolPath.MicrosoftSymbolServerPath
        };
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

    private static string SerializeStackTraces(Dictionary<int, StackSourceSample> samplesByThread,
        // ReSharper disable once SuggestBaseTypeForParameter
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
                sb.AppendLine(frameName != "UNMANAGED_CODE_TIME" ? $"    {frameName}" : "    [Native Frames]");
                stackIndex = stackSource.GetCallerIndex(stackIndex);
                frameName = stackSource.GetFrameName(stackSource.GetFrameIndex(stackIndex), false);
            }

            sb.AppendLine();
        }

        return sb.ToString();
    }
}