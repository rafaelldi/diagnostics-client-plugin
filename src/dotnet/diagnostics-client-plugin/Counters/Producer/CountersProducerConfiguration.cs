using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CountersProducerConfiguration
{
    internal CountersProducerConfiguration(int refreshInterval, CounterProviderCollection providers)
    {
        RefreshInterval = refreshInterval;
        Providers = providers;
    }

    internal int RefreshInterval { get; }
    private CounterProviderCollection Providers { get; }

    internal bool IsCounterEnabled(string provider, string counter) => Providers.Contains(provider, counter);
    internal EventPipeProvider[] EventPipeProviders => Providers.EventPipeProviders;
}