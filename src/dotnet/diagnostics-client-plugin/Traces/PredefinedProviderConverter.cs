using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using static Microsoft.Diagnostics.Tracing.Parsers.ClrTraceEventParser;
using static DiagnosticsClientPlugin.Common.Providers;
using static DiagnosticsClientPlugin.Common.DiagnosticSourceKeywords;
using static DiagnosticsClientPlugin.Common.DiagnosticSourceFilterAndPayloadSpecs;

namespace DiagnosticsClientPlugin.Traces;

internal static class PredefinedProviderConverter
{
    internal static List<TraceProvider> Convert(List<PredefinedProvider> predefinedProviders)
    {
        var providers = predefinedProviders
            .Select(it => ConvertPredefinedProvider(it))
            .Where(it => it != null)
            .ToList();

        var filterAndPayloadSpecs = GetFilterAndPayloadSpecs(predefinedProviders);
        if (filterAndPayloadSpecs.Any())
        {
            var filterAndPayloadString = string.Join("\n", filterAndPayloadSpecs);
            providers.Add(new TraceProvider(MicrosoftDiagnosticSource, EventLevel.Verbose, (long)Events,
                new Dictionary<string, string> { { FilterAndPayloadSpecs, filterAndPayloadString } })
            );
        }

        return providers;
    }

    private static TraceProvider? ConvertPredefinedProvider(PredefinedProvider provider) =>
        provider switch
        {
            PredefinedProvider.Exceptions =>
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.Exception),
            PredefinedProvider.Threads =>
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.Threading),
            PredefinedProvider.Contentions =>
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.Contention),
            PredefinedProvider.Tasks =>
                new TraceProvider(TplEventSourceProvider, EventLevel.Informational, (long)EventKeywords.All),
            PredefinedProvider.Loader =>
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.Loader),
            _ => null
        };

    private static List<string> GetFilterAndPayloadSpecs(List<PredefinedProvider> predefinedProviders)
    {
        var filterAndPayloadSpecs = new List<string>();

        if (predefinedProviders.Contains(PredefinedProvider.Http))
        {
            filterAndPayloadSpecs.AddRange(Http);
        }

        if (predefinedProviders.Contains(PredefinedProvider.AspNet))
        {
            filterAndPayloadSpecs.AddRange(AspNetCore);
        }

        if (predefinedProviders.Contains(PredefinedProvider.EF))
        {
            filterAndPayloadSpecs.AddRange(EntityFrameworkCore);
        }

        return filterAndPayloadSpecs;
    }
}