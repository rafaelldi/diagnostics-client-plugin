using System;
using System.Threading.Channels;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Generated;

namespace DiagnosticsClientPlugin.Counters.Consuming;

internal static class CountersConsumerFactory
{
    internal static AbstractFileCountersConsumer Create(
        string filePath,
        CountersFileFormat format,
        ChannelReader<ValueCounter> reader) =>
        format switch
        {
            CountersFileFormat.Csv => new ExportToCsvCountersConsumer(filePath, reader),
            CountersFileFormat.Json => new ExportToJsonCountersConsumer(filePath, reader),
            _ => throw new ArgumentOutOfRangeException(nameof(format), format, null)
        };
}