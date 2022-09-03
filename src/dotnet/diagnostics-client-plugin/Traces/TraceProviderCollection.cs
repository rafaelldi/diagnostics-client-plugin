using System;
using System.Collections.Generic;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Util;
using Microsoft.Diagnostics.NETCore.Client;
using static DiagnosticsClientPlugin.Counters.EventPipes.EventPipeProviderFactory;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProviderCollection
{
    internal EventPipeProvider[] EventPipeProviders { get; }

    internal TraceProviderCollection(string providersString, TracingProfile profile)
    {
        var providers = providersString.IsNotEmpty()
            ? TraceCollectionParser.Parse(providersString.AsSpan())
            : new List<TraceProvider>();

        var profileProviders = TraceProfileParser.ParseProfile(profile);

        MergeProviders(providers, profileProviders);

        EventPipeProviders = CreateTraceProviders(providers);
    }

    private void MergeProviders(List<TraceProvider> providers, IReadOnlyCollection<TraceProvider> profileProviders)
    {
        var providerNames = providers.Select(it => it.Name).ToList();

        providers.AddRange(profileProviders.Where(profileProvider => !providerNames.Contains(profileProvider.Name)));
    }
}