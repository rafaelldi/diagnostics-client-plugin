using System.Collections.Generic;
using System.Diagnostics.Tracing;

namespace DiagnosticsClientPlugin.Traces;

internal sealed record TraceProvider(
    string Name,
    EventLevel Level,
    long Flags,
    Dictionary<string, string>? Arguments = null
);