# Counters and Metrics

Diagnostics Client plugin allows you to monitor your application's counters and metrics. This can be very handy as a
first step in investigating a problem. With counters and metrics, you can get a bird's eye view of how your application
is performing overall. They can give you a good clue as to what the next steps should be.

## Counters

One way to monitor the performance of your application is to
use [`EventCounters`](https://learn.microsoft.com/en-us/dotnet/core/diagnostics/event-counters). The runtime itself and
some libraries provide pre-built counters, but it is also possible to create your own counter. The plugin acts as
a [dotnet-counters](https://learn.microsoft.com/en-us/dotnet/core/diagnostics/dotnet-counters) tool, allowing you to
monitor counter values or export them to a file.

### Counter monitoring

To monitor `EventCounters`, select the process and call the `Monitor Counetrs` action.

![Monitor counters dialog](counters-dialog.png){ width="450" }

{style="narrow"}
Process
: You can change the target process.

Refresh interval
: Delay between counter value updates.

Stop
: Delay between counter value updates.

Refresh interval
: You can choose to stop monitoring by manually calling the `Stop` action or automatically after a certain period of
time.

Providers
: A comma-separated list of counter providers. Each provider contains its name and, optionally, a list of
metrics `provider-name[metric1,metric2]`. If the list of metrics is not specified, all metrics from that provider will
be shown.

`Providers` is the field where you specify which counters you want to monitor. The default is `System.Runtime` and it
includes some basic counters such as `cpu-usage`, `gc-heap-size`, etc. A full list of its counters can be found in the
[documentation](https://learn.microsoft.com/en-us/dotnet/core/diagnostics/available-counters), as well as some other
providers (e.g. `Microsoft.AspNetCore.Hosting`, `Microsoft-AspNetCore-Server-Kestrel`, `System.Net.Http`, etc.).

This field allows you to select not only the provider of interest, but also to filter out some counters from the
provider. There are some examples:

- `System.Runtime`
- `System.Runtime[cpu-usage,working-set]`
- `System.Net.Http[http11-connections-current-total,http20-connections-current-total]`
- `System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting`
- `System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting[total-requests]`

### Custom counters

It is possible to create your own counters and monitor them (but currently `System.Diagnostics.Metrics` is preferred).
To do that, we have to implement an `EventSource` and then use its name as a provider. You can find a full example of
how to do this in
the [documentation](https://learn.microsoft.com/en-us/dotnet/core/diagnostics/event-counters#implement-an-eventsource).

### Export counter values

It is possible to export counter values to a file and analyze them later. To do this, call the `Export Counters` action.
In the dialog, you can select the file format, file name and output folder.

![Export counters dialog](counters-file-settings.png){ width="450" border-effect=rounded }

> You can use [CSV Plot](https://www.csvplot.com/) to plot from a CSV file.

## Metrics

A more modern approach to reporting application metrics is to use
the [`System.Diagnostics.Metrics`](https://learn.microsoft.com/en-us/dotnet/core/diagnostics/metrics) API. To monitor
them, you can use the `Metrics` section in the action dialog.

![Monitor counters dialog with metrics enabled](metrics-dialog.png){ width="450" }

The `List of metrics` field follows the same format as the `Providers` field: a comma-separated list of meters. Each
meter contains its name and, optionally, a list of metrics `meter-name[metric1,metric2]`. If no metric list is
specified, all metrics for that counter will be shown. Here are some examples:

- `OpenTelemetry.Instrumentation.Runtime`
- `OpenTelemetry.Instrumentation.Runtime[process.cpu.count]`
- `OpenTelemetry.Instrumentation.Runtime[process.cpu.count,process.runtime.dotnet.gen_0-gc.count]`
- `OpenTelemetry.Instrumentation.Runtime[process.cpu.count],OpenTelemetry.Instrumentation.AspNetCore`
- `OpenTelemetry.Instrumentation.Runtime[process.cpu.count],OpenTelemetry.Instrumentation.AspNetCore[http.server.duration]`

<seealso>
  <category ref="ext">
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/event-counters">EventCounters in .NET</a>
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/available-counters">Well-known EventCounters in .NET</a>
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/metrics">.NET metrics</a>
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/compare-metric-apis">Metric APIs comparison</a>
    <a href="https://rafaelldi.blog/posts/counters-and-metrics">What does the .NET application say — Counters and Metrics</a>
    <a href="https://rafaelldi.blog/posts/monitoring-background-task">Monitoring background task</a>
  </category>
</seealso>