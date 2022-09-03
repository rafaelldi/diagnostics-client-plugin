using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using AutoFixture.NUnit3;
using DiagnosticsClientPlugin.Counters.EventPipes;
using DiagnosticsClientPlugin.Traces;
using Microsoft.Diagnostics.Tracing.Parsers;
using NUnit.Framework;
using Should;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.Basic.Tests.EventPipes;

public class EventPipeProviderFactoryTests
{
    [Test]
    [AutoData]
    public void Create_counter_providers(int interval)
    {
        var counterProvider = "CounterProvider";

        var eventPipeProviders = EventPipeProviderFactory.CreateCounterProviders(new[] { counterProvider }, interval);

        eventPipeProviders.Length.ShouldEqual(1);
        var eventPipeProvider = eventPipeProviders.Single();
        eventPipeProvider.Name.ShouldEqual(counterProvider);
        eventPipeProvider.EventLevel.ShouldEqual(EventLevel.Informational);
        eventPipeProvider.Keywords.ShouldEqual((long)EventKeywords.None);
        eventPipeProvider.Arguments["EventCounterIntervalSec"].ShouldEqual(interval.ToString());
    }

    [Test]
    [AutoData]
    public void Create_metric_provider(string sessionId, int interval, int maxTimeSeries, int maxHistograms)
    {
        var meter = "Meter";

        var eventPipeProvider =
            EventPipeProviderFactory.CreateMetricProvider(sessionId, meter, interval, maxTimeSeries, maxHistograms);

        eventPipeProvider.ShouldNotBeNull();
        eventPipeProvider.Name.ShouldEqual(SystemDiagnosticsMetricsProvider);
        eventPipeProvider.EventLevel.ShouldEqual(EventLevel.Informational);
        eventPipeProvider.Keywords.ShouldEqual(2L);
        eventPipeProvider.Arguments["SessionId"].ShouldEqual(sessionId);
        eventPipeProvider.Arguments["Metrics"].ShouldEqual(meter);
        eventPipeProvider.Arguments["RefreshInterval"].ShouldEqual(interval.ToString());
        eventPipeProvider.Arguments["MaxTimeSeries"].ShouldEqual(maxTimeSeries.ToString());
        eventPipeProvider.Arguments["MaxHistograms"].ShouldEqual(maxHistograms.ToString());
    }

    [Test]
    [AutoData]
    public void Create_trace_providers(string name, EventLevel level, long flags, Dictionary<string, string> arguments)
    {
        var traceProviders = new[] { new TraceProvider(name, level, flags, arguments) };

        var eventPipeProviders = EventPipeProviderFactory.CreateTraceProviders(traceProviders);

        eventPipeProviders.Length.ShouldEqual(1);
        var eventPipeProvider = eventPipeProviders.Single();
        eventPipeProvider.Name.ShouldEqual(name);
        eventPipeProvider.EventLevel.ShouldEqual(level);
        eventPipeProvider.Keywords.ShouldEqual(flags);
        eventPipeProvider.Arguments.ShouldEqual(arguments);
    }

    [Test]
    public void Create_gc_provider()
    {
        var eventPipeProvider = EventPipeProviderFactory.CreateGcProvider();

        eventPipeProvider.Name.ShouldEqual(DotNetRuntimeProvider);
        eventPipeProvider.EventLevel.ShouldEqual(EventLevel.Informational);
        eventPipeProvider.Keywords.ShouldEqual((long)ClrTraceEventParser.Keywords.GC);
    }
}