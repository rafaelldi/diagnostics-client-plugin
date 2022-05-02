using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Counters.Producing;

internal static class EventPipeProviderFactory
{
    internal static EventPipeProvider[] CreateCounterProviders(IReadOnlyCollection<string> providers, int interval)
    {
        var providerArguments = new Dictionary<string, string>
        {
            ["EventCounterIntervalSec"] = interval.ToString()
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

    private static EventPipeProvider CreateMetricsProvider(int interval)
    {
        return new(
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
    }
}