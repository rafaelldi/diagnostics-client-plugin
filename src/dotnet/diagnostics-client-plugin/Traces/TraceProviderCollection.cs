using System;
using System.Collections.Generic;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Util;
using Microsoft.Diagnostics.NETCore.Client;
using static DiagnosticsClientPlugin.EventPipes.EventPipeProviderFactory;
// ReSharper disable ParameterTypeCanBeEnumerable.Local

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProviderCollection
{
    internal EventPipeProvider[] EventPipeProviders { get; }

    internal TraceProviderCollection(
        string providersString,
        TracingProfile profile,
        List<PredefinedProvider> predefinedProviderList)
    {
        var providers = providersString.IsNotEmpty()
            ? TraceCollectionParser.Parse(providersString.AsSpan())
            : new List<TraceProvider>();
        var profileProviders = TraceProfileConverter.Convert(profile);
        var predefinedProviders = PredefinedProviderConverter.Convert(predefinedProviderList);

        MergeProviders(providers, profileProviders, predefinedProviders);

        EventPipeProviders = CreateTraceProviders(providers);
    }

    private static void MergeProviders(
        ICollection<TraceProvider> providers,
        IReadOnlyCollection<TraceProvider> profileProviders,
        IReadOnlyCollection<TraceProvider> predefinedProviders)
    {
        var providerNames = providers.Select(it => it.Name).ToList();

        foreach (var profileProvider in profileProviders)
        {
            if (providerNames.Contains(profileProvider.Name))
            {
                continue;
            }
            
            providers.Add(profileProvider);
            providerNames.Add(profileProvider.Name);
        }

        foreach (var predefinedProvider in predefinedProviders)
        {
            if (providerNames.Contains(predefinedProvider.Name))
            {
                continue;
            }
            
            providers.Add(predefinedProvider);
            providerNames.Add(predefinedProvider.Name);
        }
    }
}