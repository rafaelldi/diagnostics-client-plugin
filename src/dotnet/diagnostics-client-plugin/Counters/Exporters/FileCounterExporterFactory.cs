using System;
using System.Threading.Channels;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Generated;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal static class FileCounterExporterFactory
{
    internal static AbstractFileCounterExporter Create(
        string filePath,
        CounterFileFormat format,
        ChannelReader<ValueCounter> reader) =>
        format switch
        {
            CounterFileFormat.Csv => new CsvCounterExporter(filePath, reader),
            CounterFileFormat.Json => new JsonCounterExporter(filePath, reader),
            _ => throw new ArgumentOutOfRangeException(nameof(format), format, null)
        };
}