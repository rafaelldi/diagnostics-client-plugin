using DiagnosticsClientPlugin.Counters.Producer;
using NUnit.Framework;
using Should;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.Basic.Tests.Counters;

public class CounterProviderCollectionTests
{
    [Test]
    public void Default_provider()
    {
        var collection = new CounterProviderCollection();
        var providers = collection.Providers();

        providers.Count.ShouldEqual(1);
        providers.ShouldContain(SystemRuntimeProvider);
        collection.Count().ShouldEqual(1);
        collection.Contains(SystemRuntimeProvider, "custom-counter").ShouldBeTrue();
        collection.Contains("CustomProvider", "custom-counter").ShouldBeFalse();
    }

    [Test]
    public void One_provider_without_counters()
    {
        var customProvider = "CustomProvider";
        var collection = new CounterProviderCollection(customProvider);
        var providers = collection.Providers();

        providers.Count.ShouldEqual(1);
        providers.ShouldContain(customProvider);
        collection.Count().ShouldEqual(1);
        collection.Contains(customProvider, "custom-counter").ShouldBeTrue();
        collection.Contains("AnotherCustomProvider", "custom-counter").ShouldBeFalse();
    }

    [Test]
    public void One_provider_with_counter()
    {
        var customProvider = "CustomProvider";
        var customCounter = "custom-counter";
        var collection = new CounterProviderCollection($"{customProvider}[{customCounter}]");
        var providers = collection.Providers();

        providers.Count.ShouldEqual(1);
        providers.ShouldContain(customProvider);
        collection.Count().ShouldEqual(1);
        collection.Contains(customProvider, customCounter).ShouldBeTrue();
        collection.Contains(customProvider, "another-custom-counter").ShouldBeFalse();
    }

    [Test]
    public void One_provider_with_multiple_counters()
    {
        var customProvider = "CustomProvider";
        var customCounter = "custom-counter";
        var anotherCustomCounter = "another-custom-counter";
        var collection = new CounterProviderCollection($"{customProvider}[{customCounter},{anotherCustomCounter}]");
        var providers = collection.Providers();

        providers.Count.ShouldEqual(1);
        providers.ShouldContain(customProvider);
        collection.Count().ShouldEqual(1);
        collection.Contains(customProvider, customCounter).ShouldBeTrue();
        collection.Contains(customProvider, anotherCustomCounter).ShouldBeTrue();
    }

    [Test]
    public void Multiple_providers_with_counters()
    {
        var customProvider = "CustomProvider";
        var customCounter = "custom-counter";
        var anotherCustomProvider = "AnotherCustomProvider";
        var anotherCustomCounter = "another-custom-counter";
        var collection = new CounterProviderCollection(
            $"{customProvider}[{customCounter}],{anotherCustomProvider}[{anotherCustomCounter}]");
        var providers = collection.Providers();

        providers.Count.ShouldEqual(2);
        providers.ShouldContain(customProvider);
        providers.ShouldContain(anotherCustomProvider);
        collection.Count().ShouldEqual(2);
        collection.Contains(customProvider, customCounter).ShouldBeTrue();
        collection.Contains(anotherCustomProvider, anotherCustomCounter).ShouldBeTrue();
    }
}