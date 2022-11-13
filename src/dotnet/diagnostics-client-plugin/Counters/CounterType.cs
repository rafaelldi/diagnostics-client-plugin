using System;

namespace DiagnosticsClientPlugin.Counters;

internal enum CounterType
{
    Metric,
    Rate
}

internal static class CounterTypeHelper
{
    private const string Metric = "Metric";
    private const string Rate = "Rate";

    internal static string ToValue(this CounterType type)
    {
        return type switch
        {
            CounterType.Metric => Metric,
            CounterType.Rate => Rate,
            _ => throw new ArgumentOutOfRangeException(nameof(type), type, null)
        };
    }
}