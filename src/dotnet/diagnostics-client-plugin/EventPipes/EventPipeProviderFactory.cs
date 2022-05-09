using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Traces;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Counters.EventPipes;

internal static class EventPipeProviderFactory
{
    internal const string SystemRuntimeProvider = "System.Runtime";
    internal const string SampleProfilerProvider = "Microsoft-DotNETCore-SampleProfiler";
    internal const string DotNetRuntimeProvider = "Microsoft-Windows-DotNETRuntime";

    private const string IntervalArgument = "EventCounterIntervalSec";

    internal static EventPipeProvider[] CreateCounterProviders(IReadOnlyCollection<string> providers, int interval)
    {
        var providerArguments = new Dictionary<string, string>
        {
            [IntervalArgument] = interval.ToString()
        };

        return providers.Select(it => CreateCounterProvider(it, providerArguments)).ToArray();
    }

    private static EventPipeProvider CreateCounterProvider(
        string provider,
        Dictionary<string, string> arguments) =>
        new(
            provider,
            EventLevel.Informational,
            (long)EventKeywords.None,
            arguments
        );

    private static EventPipeProvider CreateMetricsProvider(int interval) =>
        new(
            "System.Diagnostics.Metrics",
            EventLevel.Informational,
            2L,
            new Dictionary<string, string>
            {
                ["SessionId"] = "",
                ["Metrics"] = "",
                ["RefreshInterval"] = interval.ToString(),
                ["MaxTimeSeries"] = 1000.ToString(),
                ["MaxHistograms"] = 10.ToString()
            }
        );

    internal static EventPipeProvider[] CreateTraceProviders(IReadOnlyCollection<TraceProvider> providers) =>
        providers.Select(it => new EventPipeProvider(
                it.Name,
                it.Level,
                it.Flags,
                it.Arguments
            ))
            .ToArray();
}