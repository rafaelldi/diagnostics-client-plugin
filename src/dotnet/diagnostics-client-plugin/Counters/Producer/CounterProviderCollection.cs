using System;
using System.Collections.Generic;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CounterProviderCollection
{
    private const string SystemRuntimeEventSource = "System.Runtime";

    private readonly Dictionary<string, List<string>?> _providers = new();

    internal CounterProviderCollection(string providersString)
    {
        if (string.IsNullOrEmpty(providersString))
        {
            _providers.Add(SystemRuntimeEventSource, null);
            return;
        }

        ParseProviders(providersString.AsSpan());
    }

    internal IReadOnlyCollection<string> ProviderNames => _providers.Keys;

    private void ParseProviders(in ReadOnlySpan<char> providersString)
    {
        var insideCounterList = false;
        var providerStartIndex = 0;
        for (var i = 0; i < providersString.Length; i++)
        {
            if (providersString[i] == '[')
            {
                insideCounterList = true;
                continue;
            }

            if (providersString[i] == ']')
            {
                insideCounterList = false;
                continue;
            }

            if (providersString[i] != ',' || insideCounterList)
            {
                continue;
            }

            var provider = ParseProvider(providersString.Slice(providerStartIndex, i - providerStartIndex));
            _providers.Add(provider.Provider, provider.Metrics);
            providerStartIndex = i + 1;
        }

        var lastProvider =
            ParseProvider(providersString.Slice(providerStartIndex, providersString.Length - providerStartIndex));
        _providers.Add(lastProvider.Provider, lastProvider.Metrics);
    }

    private static (string Provider, List<string>? Metrics) ParseProvider(in ReadOnlySpan<char> providerString)
    {
        var counterListIndex = providerString.IndexOf('[');
        if (counterListIndex == -1)
        {
            return (providerString.ToString(), null);
        }

        var providerName = providerString.Slice(0, counterListIndex);
        var counterListLength = providerString.Length - counterListIndex - 2;
        var counterList = providerString.Slice(counterListIndex + 1, counterListLength);
        var counters = new List<string>();
        var counterStartIndex = 0;
        for (var i = 0; i < counterList.Length; i++)
        {
            if (counterList[i] != ',')
            {
                continue;
            }

            counters.Add(counterList.Slice(counterStartIndex, i - counterStartIndex).ToString());
            counterStartIndex = i + 1;
        }

        counters.Add(counterList.Slice(counterStartIndex, counterList.Length - counterStartIndex).ToString());

        return (providerName.ToString(), counters);
    }

    internal bool Contains(string provider, string counter)
    {
        if (!_providers.TryGetValue(provider, out var counters))
        {
            return false;
        }

        return counters is null || counters.Contains(counter);
    }
}