using System;
using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using static Microsoft.Diagnostics.Tracing.Parsers.ClrTraceEventParser;
using static DiagnosticsClientPlugin.Common.Providers;
using static DiagnosticsClientPlugin.Common.DiagnosticSourceKeywords;
using static DiagnosticsClientPlugin.Common.DiagnosticSourceFilterAndPayloadSpecs;

namespace DiagnosticsClientPlugin.Traces;

internal static class PredefinedProviderParser
{
    internal static List<TraceProvider> Parse(List<PredefinedProvider> providers) =>
        providers.Select(it => ParseProvider(it)).ToList();

    private static TraceProvider ParseProvider(PredefinedProvider provider) =>
        provider switch
        {
            PredefinedProvider.Http =>
                new TraceProvider(MicrosoftDiagnosticSource, EventLevel.Verbose, (long)Events,
                    new Dictionary<string, string> { { FilterAndPayloadSpecs, Http } }),
            PredefinedProvider.AspNet =>
                new TraceProvider(MicrosoftDiagnosticSource, EventLevel.Verbose, (long)Events,
                    new Dictionary<string, string> { { FilterAndPayloadSpecs, AspNetCore } }),
            PredefinedProvider.EF =>
                new TraceProvider(MicrosoftDiagnosticSource, EventLevel.Verbose, (long)Events,
                    new Dictionary<string, string> { { FilterAndPayloadSpecs, EntityFrameworkCore } }),
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
            _ => throw new ArgumentOutOfRangeException(nameof(provider), provider, null)
        };
}