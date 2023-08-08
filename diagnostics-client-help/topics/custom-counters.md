# Create custom counters

This page shows how to create a new custom `EventCounter` for your project and monitor it with the plugin.

To add your own counter to the application, you need to implement
the [System.Diagnostics.Tracing.EventSource](https://learn.microsoft.com/en-us/dotnet/api/system.diagnostics.tracing.eventsource).
The simplest one may look like this:

```c#
[EventSource(Name = "Example.MyEventCounterSource")]
public sealed class MyEventCounterSource : EventSource
{
    // 👇 Static instance of this EventSource
    public static readonly MyEventCounterSource Instance = new();

    // 👇 Our counter. Each EventSource may contain several counters
    private readonly IncrementingEventCounter _myCounter;

    private MyEventCounterSource() =>
        _myCounter = new IncrementingEventCounter("my-counter", this)
        {
            DisplayName = "My Incrementing Counter"
        };

    // 👇 Increment the counter when this method is called
    public void Up() => _myCounter.Increment();

    protected override void Dispose(bool disposing)
    {
        _myCounter.Dispose();
        base.Dispose(disposing);
    }
}
```

The counter can then be updated from any point of the application (e.g. from special middleware or after some business
events occur). In this example, I will create a plain console application and increment the counter in a loop.

```c#
Task.Run(async () => await CounterProducingTask());

Console.ReadKey();

async Task CounterProducingTask()
{
    while (true)
    {
        // 👇 Increment the counter every 200 ms
        MyEventCounterSource.Instance.Up();
        await Task.Delay(200);
    }
}
```

To watch a new counter, you need to specify the name of the `EventSource` (`Example.MyEventCounterSource`) in
the `Providers` field.

![Monitor counters dialog with custom provider](custom-event-provider.png){ width="450" }

<seealso>
  <category ref="ext">
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/event-counters">EventCounters in .NET</a>
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/event-counter-perf">Measure performance using EventCounters in .NET Core</a>
  </category>
</seealso>