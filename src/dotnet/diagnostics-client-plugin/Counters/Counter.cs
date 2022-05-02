using System;

namespace DiagnosticsClientPlugin.Counters.Common;

internal record Counter(
    DateTime TimeStamp,
    string Name,
    string ProviderName,
    double Value,
    CounterType Type
);