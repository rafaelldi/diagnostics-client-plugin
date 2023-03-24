using System;
using DiagnosticsClientPlugin.Counters.Producer;
using NUnit.Framework;
using Should;

namespace DiagnosticsClientPlugin.Tests.Counters;

public class CounterCollectionParserTests
{
    [Test]
    public void Parse_empty_string()
    {
        var countersString = string.Empty;
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        counters.ShouldBeEmpty();
    }

    [Test]
    public void Parse_empty_string_with_delimiter()
    {
        var countersString = ",";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        counters.ShouldBeEmpty();
    }

    [Test]
    public void Parse_one_provider()
    {
        var countersString = "System.Runtime";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());

        counters.Keys.ShouldContain("System.Runtime");
        counters["System.Runtime"].ShouldBeNull();
    }

    [Test]
    public void Parse_one_provider_with_empty_metrics()
    {
        var countersString = "System.Runtime[]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());

        counters.Keys.ShouldContain("System.Runtime");
        counters["System.Runtime"].ShouldBeNull();
    }

    [Test]
    public void Parse_one_provider_with_empty_metrics_and_delimiter()
    {
        var countersString = "System.Runtime[,]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());

        counters.Keys.ShouldContain("System.Runtime");
        counters["System.Runtime"].ShouldBeNull();
    }

    [Test]
    public void Parse_one_provider_with_metrics()
    {
        var countersString = "System.Runtime[cpu-usage,alloc-rate,exception-count]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());

        counters.Keys.ShouldContain("System.Runtime");
        var provider = counters["System.Runtime"];
        provider.ShouldNotBeNull();
        provider.ShouldNotBeEmpty();
        provider.ShouldContain("cpu-usage");
        provider.ShouldContain("alloc-rate");
        provider.ShouldContain("exception-count");
    }

    [Test]
    public void Parse_one_provider_with_empty_second_metric()
    {
        var countersString = "System.Runtime[cpu-usage,]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        var provider = counters["System.Runtime"];

        provider.ShouldNotBeNull();
        provider.ShouldNotBeEmpty();
        provider.ShouldContain("cpu-usage");
        provider!.Count.ShouldEqual(1);
    }

    [Test]
    public void Parse_one_provider_with_empty_first_metric()
    {
        var countersString = "System.Runtime[,cpu-usage]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        var provider = counters["System.Runtime"];

        provider.ShouldNotBeNull();
        provider.ShouldNotBeEmpty();
        provider.ShouldContain("cpu-usage");
        provider!.Count.ShouldEqual(1);
    }

    [Test]
    public void Parse_multiple_providers_with_empty_second_one()
    {
        var countersString = "System.Runtime[cpu-usage],";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        var provider = counters["System.Runtime"];

        provider.ShouldNotBeNull();
        provider.ShouldNotBeEmpty();
        provider.ShouldContain("cpu-usage");
        counters.Count.ShouldEqual(1);
    }

    [Test]
    public void Parse_multiple_providers_with_empty_first_one()
    {
        var countersString = ",System.Runtime[cpu-usage]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        var provider = counters["System.Runtime"];

        provider.ShouldNotBeNull();
        provider.ShouldNotBeEmpty();
        provider.ShouldContain("cpu-usage");
        counters.Count.ShouldEqual(1);
    }

    [Test]
    public void Parse_multiple_providers()
    {
        var countersString = "System.Runtime[cpu-usage],MyEventCounterSource";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        var provider = counters["System.Runtime"];

        provider.ShouldNotBeNull();
        provider.ShouldNotBeEmpty();
        provider.ShouldContain("cpu-usage");
        counters.Keys.ShouldContain("MyEventCounterSource");
        counters["MyEventCounterSource"].ShouldBeNull();
    }

    [Test]
    public void Parse_multiple_providers_with_metrics()
    {
        var countersString = "System.Runtime[cpu-usage],MyEventCounterSource[my-counter]";
        var counters = CounterCollectionParser.Parse(countersString.AsSpan());
        var runtimeProvider = counters["System.Runtime"];

        runtimeProvider.ShouldNotBeNull();
        runtimeProvider.ShouldNotBeEmpty();
        runtimeProvider.ShouldContain("cpu-usage");
        var customProvider = counters["MyEventCounterSource"];
        customProvider.ShouldNotBeNull();
        customProvider.ShouldNotBeEmpty();
        customProvider.ShouldContain("my-counter");
    }
}