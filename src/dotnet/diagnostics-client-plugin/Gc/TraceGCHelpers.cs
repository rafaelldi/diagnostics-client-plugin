using System;
using Microsoft.Diagnostics.Tracing.Parsers.Clr;

namespace DiagnosticsClientPlugin.Gc;

internal static class TraceGcHelpers
{
    internal static string ToValue(this GCReason gcReason) =>
        gcReason switch {
            GCReason.AllocSmall => "AllocSmall",
            GCReason.Induced => "Induced",
            GCReason.LowMemory => "LowMemory",
            GCReason.Empty => "Empty",
            GCReason.AllocLarge => "AllocLarge",
            GCReason.OutOfSpaceSOH => "OutOfSpaceSOH",
            GCReason.OutOfSpaceLOH => "OutOfSpaceLOH",
            GCReason.InducedNotForced => "InducedNotForced",
            GCReason.Internal => "Internal",
            GCReason.InducedLowMemory => "InducedLowMemory",
            GCReason.InducedCompacting => "InducedCompacting",
            GCReason.LowMemoryHost => "LowMemoryHost",
            GCReason.PMFullGC => "PMFullGC",
            GCReason.LowMemoryHostBlocking => "LowMemoryHostBlocking",
            _ => throw new ArgumentOutOfRangeException(nameof(gcReason), gcReason, null)
        };
}