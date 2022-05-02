using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Counters.Producing;

internal sealed class CountersProducerConfiguration
{
    internal CountersProducerConfiguration(int refreshInterval, CounterProviderCollection providers)
    {
        RefreshInterval = refreshInterval;
        Providers = providers;
        EventPipeProviders = EventPipeProviderFactory.CreateCounterProviders(providers.ProviderNames, refreshInterval);
    }

    internal int RefreshInterval { get; }
    internal EventPipeProvider[] EventPipeProviders { get; }
    private CounterProviderCollection Providers { get; }

    internal bool IsCounterEnabled(string provider, string counter)
    {
        return Providers.Contains(provider, counter);
    }
}