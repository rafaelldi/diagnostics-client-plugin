using System;

namespace DiagnosticsClientPlugin.Counters.Common;

internal readonly record struct ValueCounter(
    DateTime TimeStamp,
    string Name,
    string DisplayName,
    string ProviderName,
    double Value,
    CounterType Type,
    string? Tags
);