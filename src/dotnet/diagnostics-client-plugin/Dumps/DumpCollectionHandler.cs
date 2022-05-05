using System;
using System.IO;
using DiagnosticsClientPlugin.Generated;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;
using JetBrains.Util;
using Microsoft.Diagnostics.NETCore.Client;
using DumpType = Microsoft.Diagnostics.NETCore.Client.DumpType;

namespace DiagnosticsClientPlugin.Dumps;

[SolutionComponent]
internal sealed class DumpCollectionHandler
{
    private readonly ILogger _logger;

    public DumpCollectionHandler(ISolution solution, ILogger logger)
    {
        _logger = logger;
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.CollectDump.Set((_, command) => Collect(command));
    }

    private RdTask<DumpCollectionResult> Collect(CollectDumpCommand command)
    {
        try
        {
            var client = new DiagnosticsClient(command.Pid);
            var type = Map(command.Type);
            var path = Path.Combine(command.OutFolder, command.Filename);
            client.WriteDump(type, path, command.Diag);

            return RdTask<DumpCollectionResult>.Successful(new DumpCollectionResult(path));
        }
        catch (Exception ex) when
            (ex is FileNotFoundException
                or DirectoryNotFoundException
                or UnauthorizedAccessException
                or PlatformNotSupportedException
                or UnsupportedCommandException
                or InvalidDataException
                or InvalidOperationException
                or NotSupportedException
                or DiagnosticsClientException)
        {
            _logger.Error(ex);
            return RdTask<DumpCollectionResult>.Faulted(ex);
        }
    }

    private static DumpType Map(Generated.DumpType type) =>
        type switch
        {
            Generated.DumpType.Full => DumpType.Full,
            Generated.DumpType.Heap => DumpType.WithHeap,
            Generated.DumpType.Triage => DumpType.Triage,
            Generated.DumpType.Mini => DumpType.Normal,
            _ => throw new ArgumentOutOfRangeException()
        };
}