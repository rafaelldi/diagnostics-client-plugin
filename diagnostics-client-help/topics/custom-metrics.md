# Create custom metrics

This page shows how to publish custom OpenTelemetry metrics for your project and monitor them with the plugin.

To add a custom metric, you need to create a new `Meter` and an instrument from that meter. You can use `static` fields
or [newly added](https://github.com/dotnet/runtime/issues/77514) `IMeterFactory` interface.

```c#
static Meter meter = new Meter("Example.MyMeter");
static Counter<int> counter = meter.CreateCounter<int>("my-counter");
```

You can then increment the metric from the proper place in the application.

```c#
async Task StartMetricProducingTask()
{
    while (true)
    {
        // 👇 Increment the counter every 200 ms
        counter.Add(1);
        await Task.Delay(200);
    }
}
```

There are different types of instruments you can create:

* `Counter`
* `UpDownCounter`
* `ObservableCounter`
* `ObservableUpDownCounter`
* `ObservableGauge`
* `Histogram`

More information about them and some best practices you can find in
the [.NET documentation](https://learn.microsoft.com/en-us/dotnet/core/diagnostics/metrics-instrumentation).

To watch new metrics, specify your meter name in the `Metrics` field.

![Monitor counters dialog with custom meter](custom-meter.png){ width="450" }

<seealso>
  <category ref="ext">
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/metrics-instrumentation">Creating Metrics</a>
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/observability-with-otel">.NET observability with OpenTelemetry</a>
  </category>
</seealso>