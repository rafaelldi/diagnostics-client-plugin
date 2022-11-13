using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Traces;
using Microsoft.Diagnostics.NETCore.Client;
using Microsoft.Diagnostics.Tracing.Parsers;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.EventPipes;

internal static class EventPipeProviderFactory
{
    private const string IntervalArgument = "EventCounterIntervalSec";
    private const string SessionIdArgument = "SessionId";
    private const string MetricsArgument = "Metrics";
    private const string RefreshIntervalArgument = "RefreshInterval";
    private const string MaxTimeSeriesArgument = "MaxTimeSeries";
    private const string MaxHistogramsArgument = "MaxHistograms";

    internal static EventPipeProvider[] CreateCounterProviders(IReadOnlyCollection<string> providers, int interval)
    {
        var providerArguments = new Dictionary<string, string>
        {
            [IntervalArgument] = interval.ToString()
        };

        return providers.Select(it => new EventPipeProvider(
            it,
            EventLevel.Informational,
            (long)EventKeywords.None,
            providerArguments
        )).ToArray();
    }

    internal static EventPipeProvider CreateMetricProvider(
        string sessionId,
        string metrics,
        int interval,
        int maxTimeSeries,
        int maxHistograms
    ) =>
        new(
            SystemDiagnosticsMetricsProvider,
            EventLevel.Informational,
            2L,
            new Dictionary<string, string>
            {
                [SessionIdArgument] = sessionId,
                [MetricsArgument] = metrics,
                [RefreshIntervalArgument] = interval.ToString(),
                [MaxTimeSeriesArgument] = maxTimeSeries.ToString(),
                [MaxHistogramsArgument] = maxHistograms.ToString()
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

    internal static EventPipeProvider CreateSampleProvider() => new(
        SampleProfilerProvider,
        EventLevel.Informational
    );

    internal static EventPipeProvider CreateGcProvider() => new(
        DotNetRuntimeProvider,
        EventLevel.Informational,
        (long)ClrTraceEventParser.Keywords.GC
    );

    internal static EventPipeProvider CreateGcHeapCollect() => new(
        DotNetRuntimeProvider,
        EventLevel.Informational,
        (long)ClrTraceEventParser.Keywords.GCHeapCollect
    );
}