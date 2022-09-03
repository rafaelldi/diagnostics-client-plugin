using System;
using System.Diagnostics.Tracing;
using DiagnosticsClientPlugin.Generated;
using static Microsoft.Diagnostics.Tracing.Parsers.ClrTraceEventParser;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.Traces;

internal static class TraceProfileParser
{
    internal static TraceProvider[] ParseProfile(TracingProfile profile) =>
        profile switch
        {
            TracingProfile.None => Array.Empty<TraceProvider>(),
            TracingProfile.CpuSampling => new[]
            {
                new TraceProvider(SampleProfilerProvider, EventLevel.Informational, 0xF00000000000),
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.Default)
            },
            TracingProfile.GcVerbose => new[]
            {
                new TraceProvider(
                    DotNetRuntimeProvider,
                    EventLevel.Verbose,
                    (long)Keywords.GC | (long)Keywords.GCHandle | (long)Keywords.Exception
                )
            },
            TracingProfile.GcCollect => new[]
            {
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.GC)
            },
            _ => throw new ArgumentOutOfRangeException(nameof(profile), profile, null)
        };
}