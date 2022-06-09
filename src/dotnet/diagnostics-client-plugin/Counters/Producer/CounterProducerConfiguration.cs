using System.Collections.Generic;
using DiagnosticsClientPlugin.Counters.EventPipes;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CounterProducerConfiguration
{
    internal string SessionId { get; }
    internal int RefreshInterval { get; }
    internal IReadOnlyCollection<EventPipeProvider> EventPipeProviders { get; }
    private readonly CounterProviderCollection _counterProviders;

    internal CounterProducerConfiguration(
        string sessionId,
        string listOfCounterProviders,
        string? listOfMetrics,
        int refreshInterval,
        int maxTimeSeries,
        int maxHistograms)
    {
        SessionId = sessionId;
        RefreshInterval = refreshInterval;

        if (string.IsNullOrEmpty(listOfCounterProviders) && string.IsNullOrEmpty(listOfMetrics))
        {
            _counterProviders = new CounterProviderCollection();
            EventPipeProviders = EventPipeProviderFactory.CreateCounterProviders(_counterProviders.Providers(), 
                refreshInterval);
        }
        else
        {
            _counterProviders = new CounterProviderCollection(listOfCounterProviders);

            var eventPipeProviderCount = listOfMetrics is not null
                ? _counterProviders.Count() + 1
                : _counterProviders.Count();
            var eventPipeProviders = new List<EventPipeProvider>(eventPipeProviderCount);

            var counterProviders = EventPipeProviderFactory.CreateCounterProviders(_counterProviders.Providers(), 
                refreshInterval);
            eventPipeProviders.AddRange(counterProviders);

            if (listOfMetrics is not null)
            {
                var metricCollection = new MetricCollection(listOfMetrics);
                var metricProvider = EventPipeProviderFactory.CreateMetricProvider(sessionId, metricCollection.Metrics,
                    refreshInterval, maxTimeSeries, maxHistograms);
                eventPipeProviders.Add(metricProvider);
            }

            EventPipeProviders = eventPipeProviders.AsReadOnly();
        }
    }

    internal bool IsCounterEnabled(string provider, string counter) => _counterProviders.Contains(provider, counter);
}