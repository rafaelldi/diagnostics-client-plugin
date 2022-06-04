using System;
using System.Collections.Generic;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CounterProviderCollection
{
    private readonly Dictionary<string, List<string>?> _counterProviders;

    internal CounterProviderCollection(string counterProviderCollectionString)
    {
        if (string.IsNullOrEmpty(counterProviderCollectionString))
        {
            _counterProviders = new Dictionary<string, List<string>?>
            {
                [SystemRuntimeProvider] = null
            };
        }
        else
        {
            _counterProviders = CounterCollectionParser.Parse(counterProviderCollectionString.AsSpan());
        }
    }

    internal IReadOnlyCollection<string> Providers() => _counterProviders.Keys;

    internal int Count() => _counterProviders.Count;

    internal bool Contains(string provider, string counter)
    {
        if (!_counterProviders.TryGetValue(provider, out var counters))
        {
            return false;
        }

        return counters is null || counters.Contains(counter);
    }
}