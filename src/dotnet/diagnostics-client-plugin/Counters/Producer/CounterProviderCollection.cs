using System;
using System.Collections.Generic;
using DiagnosticsClientPlugin.Counters.EventPipes;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CounterProviderCollection
{
    private readonly Dictionary<string, List<string>?> _providers = new();
    internal EventPipeProvider[] EventPipeProviders { get; }

    internal CounterProviderCollection(string providersString, int refreshInterval)
    {
        if (string.IsNullOrEmpty(providersString))
        {
            _providers.Add(EventPipeProviderFactory.SystemRuntimeProvider, null);
        }
        else
        {
            ParseProviders(providersString.AsSpan());
        }

        EventPipeProviders = EventPipeProviderFactory.CreateCounterProviders(_providers.Keys, refreshInterval);
    }

    private void ParseProviders(in ReadOnlySpan<char> providersString)
    {
        var insideCounterList = false;
        var providerStartIndex = 0;
        (string Provider, List<string>? Metrics)? provider;

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

            provider = ParseProvider(providersString.Slice(providerStartIndex, i - providerStartIndex).Trim());
            if (provider.HasValue)
            {
                _providers.Add(provider.Value.Provider, provider.Value.Metrics);
            }

            providerStartIndex = i + 1;
        }

        provider = ParseProvider(providersString.Slice(providerStartIndex).Trim());
        if (provider.HasValue)
        {
            _providers.Add(provider.Value.Provider, provider.Value.Metrics);
        }
    }

    private static (string Provider, List<string>? Metrics)? ParseProvider(in ReadOnlySpan<char> providerString)
    {
        if (providerString.IsEmpty)
        {
            return null;
        }

        var counterListIndex = providerString.IndexOf('[');
        if (counterListIndex == -1)
        {
            return (providerString.ToString(), null);
        }

        var providerName = providerString.Slice(0, counterListIndex).Trim();
        var counters = new List<string>();

        var counterListLength = providerString.Length - counterListIndex - 2;
        var counterList = providerString.Slice(counterListIndex + 1, counterListLength);
        var delimiterIndex = counterList.IndexOf(',');
        while (delimiterIndex != -1)
        {
            counters.Add(counterList.Slice(0, delimiterIndex).Trim().ToString());
            counterList = counterList.Slice(delimiterIndex + 1);
            delimiterIndex = counterList.IndexOf(',');
        }

        counters.Add(counterList.Trim().ToString());

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