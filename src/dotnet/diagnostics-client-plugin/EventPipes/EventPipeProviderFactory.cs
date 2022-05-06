using System;
using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using Microsoft.Diagnostics.NETCore.Client;
using static Microsoft.Diagnostics.Tracing.Parsers.ClrTraceEventParser;

namespace DiagnosticsClientPlugin.Counters.EventPipes;

internal static class EventPipeProviderFactory
{
    public const string SystemRuntimeProvider = "System.Runtime";
    private const string SampleProfilerProvider = "Microsoft-DotNETCore-SampleProfiler";
    private const string DotNetRuntimeProvider = "Microsoft-Windows-DotNETRuntime";

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

    internal static EventPipeProvider[] CreateProfileProvider(TracingProfile profile) =>
        profile switch
        {
            TracingProfile.None => Array.Empty<EventPipeProvider>(),
            TracingProfile.CpuSampling => new[]
            {
                new EventPipeProvider(SampleProfilerProvider, EventLevel.Informational),
                new EventPipeProvider(
                    DotNetRuntimeProvider,
                    EventLevel.Informational,
                    (long)Keywords.Default
                )
            },
            TracingProfile.GcVerbose => new[]
            {
                new EventPipeProvider(
                    DotNetRuntimeProvider,
                    EventLevel.Verbose,
                    (long)Keywords.GC | (long)Keywords.GCHandle | (long)Keywords.Exception
                )
            },
            TracingProfile.GcCollect => new[]
            {
                new EventPipeProvider(
                    DotNetRuntimeProvider,
                    EventLevel.Informational,
                    (long)Keywords.GC
                )
            },
            _ => throw new ArgumentOutOfRangeException(nameof(profile), profile, null)
        };
}