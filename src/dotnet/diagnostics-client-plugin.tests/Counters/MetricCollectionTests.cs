using DiagnosticsClientPlugin.Counters.Producer;
using NUnit.Framework;
using Should;

namespace DiagnosticsClientPlugin.Tests.Counters;

public class MetricCollectionTests
{
    [Test]
    public void Empty_meter_list()
    {
        var collection = new MetricCollection(string.Empty);
        collection.Metrics.ShouldBeEmpty();
    }

    [Test]
    public void One_meter_without_metrics()
    {
        var customMeter = "CustomMeter";
        var collection = new MetricCollection(customMeter);
        collection.Metrics.ShouldEqual(customMeter);
    }

    [Test]
    public void One_meter_with_metric()
    {
        var customMeter = "CustomMeter";
        var customMetric = "custom-metric";
        var collection = new MetricCollection($"{customMeter}[{customMetric}]");
        collection.Metrics.ShouldEqual($"{customMeter}\\{customMetric}");
    }

    [Test]
    public void One_meter_with_multiple_metrics()
    {
        var customMeter = "CustomMeter";
        var customMetric = "custom-metric";
        var anotherCustomMetric = "another-custom-metric";
        var collection = new MetricCollection($"{customMeter}[{customMetric},{anotherCustomMetric}]");
        collection.Metrics.ShouldEqual($"{customMeter}\\{customMetric},{customMeter}\\{anotherCustomMetric}");
    }

    [Test]
    public void Multiple_meters_with_metrics()
    {
        var customMeter = "CustomMeter";
        var customMetric = "custom-metric";
        var anotherCustomMeter = "AnotherCustomMeter";
        var anotherCustomMetric = "another-custom-metric";
        var collection = new MetricCollection(
            $"{customMeter}[{customMetric}],{anotherCustomMeter}[{anotherCustomMetric}]");
        collection.Metrics.ShouldEqual($"{customMeter}\\{customMetric},{anotherCustomMeter}\\{anotherCustomMetric}");
    }
}