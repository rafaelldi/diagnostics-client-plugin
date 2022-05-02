using System;
using System.Threading.Channels;
using DiagnosticsClientPlugin.Generated;
using Counter = DiagnosticsClientPlugin.Counters.Common.Counter;

namespace DiagnosticsClientPlugin.Counters.Consuming;

internal static class CountersConsumerFactory
{
    internal static AbstractFileCountersConsumer Create(
        string filePath,
        CountersFileFormat format,
        ChannelReader<Counter> reader) =>
        format switch
        {
            CountersFileFormat.Csv => new ExportToCsvCountersConsumer(filePath, reader),
            CountersFileFormat.Json => new ExportToJsonCountersConsumer(filePath, reader),
            _ => throw new ArgumentOutOfRangeException(nameof(format), format, null)
        };
}