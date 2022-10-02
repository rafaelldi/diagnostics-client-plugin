using System.Linq;
using AutoFixture.NUnit3;
using DiagnosticsClientPlugin.Counters.Producer;
using NUnit.Framework;
using Should;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.Basic.Tests.Counters;

public class CounterProducerConfigurationTests
{
    [Test]
    [AutoData]
    public void Default_configuration(string sessionId, int refreshInterval)
    {
        var configuration = new CounterProducerConfiguration(sessionId, string.Empty, null, refreshInterval, 0, 0);

        configuration.SessionId.ShouldEqual(sessionId);
        configuration.RefreshInterval.ShouldEqual(refreshInterval);
        configuration.EventPipeProviders.Count.ShouldEqual(1);
        var provider = configuration.EventPipeProviders.Single();
        provider.Name.ShouldEqual(SystemRuntimeProvider);
    }

    [Test]
    [AutoData]
    public void Counters_only_configuration(string sessionId, int refreshInterval)
    {
        var counterProvider = "CounterProvider";
        var configuration = new CounterProducerConfiguration(sessionId, counterProvider, null, refreshInterval, 0, 0);

        configuration.EventPipeProviders.Count.ShouldEqual(1);
        var provider = configuration.EventPipeProviders.Single();
        provider.Name.ShouldEqual(counterProvider);
    }

    [Test]
    [AutoData]
    public void Metrics_only_configuration(string sessionId, int refreshInterval)
    {
        var meter = "Meter";
        var configuration = new CounterProducerConfiguration(sessionId, string.Empty, meter, refreshInterval, 0, 0);

        configuration.EventPipeProviders.Count.ShouldEqual(1);
        var provider = configuration.EventPipeProviders.Single();
        provider.Name.ShouldEqual(SystemDiagnosticsMetricsProvider);
    }

    [Test]
    [AutoData]
    public void Counters_and_metrics_configuration(string sessionId, int refreshInterval)
    {
        var counterProvider = "CounterProvider";
        var meter = "Meter";
        var configuration = new CounterProducerConfiguration(sessionId, counterProvider, meter, refreshInterval, 0, 0);

        configuration.EventPipeProviders.Count.ShouldEqual(2);
        var providerNames = configuration.EventPipeProviders.Select(it => it.Name).ToList();
        providerNames.ShouldContain(counterProvider);
        providerNames.ShouldContain(SystemDiagnosticsMetricsProvider);
    }
}