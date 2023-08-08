# Metrics examples

This page shows you how to add OpenTelemetry metrics to your project and monitor them.

## Add Instrumentation

To add metrics to the project, you need to add some OpenTelemetry nuget packages (
e.g. `OpenTelemetry.Extensions.Hosting`, `OpenTelemetry.Instrumentation.AspNetCore`, `OpenTelemetry.Exporter.Console`).
Instrumentation libraries provide metrics (and traces) for the various frameworks and libraries in your project. I'll
show some common examples. You can also [create an instrumentation by yourself](custom-metrics.md) and add custom
metrics.

After installing packages, register OpenTelemetry:

```c#
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddOpenTelemetry()
    .WithMetrics(x =>
    {
        x.SetResourceBuilder(ResourceBuilder.CreateDefault().AddService(builder.Environment.ApplicationName))
            .AddAspNetCoreInstrumentation()
            .AddRuntimeInstrumentation()
            .AddHttpClientInstrumentation()
            .AddProcessInstrumentation()
            .AddMeter("Microsoft.AspNetCore.Hosting")
            .AddMeter("Microsoft.AspNetCore.Server.Kestrel")
            .AddOtlpExporter();
    });
```

To watch your metrics, set the added meters in the `Metrics` field and click the `Watch` button (as with
`EventCounters`, you can specify particular metrics for each meter in a format `meter-name[metric1,metric2]`).

![Monitor counters dialog with list of meters](common-meters.png){ width="700" border-effect=rounded }

## Common Meters

There are two main repositories where you can find some popular instrumentation
libraries: [OpenTelemetry .NET](https://github.com/open-telemetry/opentelemetry-dotnet)
and [OpenTelemetry .NET Contrib](https://github.com/open-telemetry/opentelemetry-dotnet-contrib). I'll give some
examples below, but this is a rapidly developing area and everything can be changed.

### ASP.NET Core

[From ASP.NET Core 8](https://devblogs.microsoft.com/dotnet/asp-net-core-updates-in-dotnet-8-preview-4/#asp-net-core-metrics),
there are some built-in metrics. To enable them, add
meters `Microsoft.AspNetCore.Hosting`, `Microsoft.AspNetCore.Server.Kestrel` and `Microsoft.AspNetCore.Http.Connections`
with `AddMeter()` method. See the full list of added metrics
in [this issue](https://github.com/dotnet/aspnetcore/issues/47536).

| Meter                                 | Metric                                     | Description                                                                                                               |
|---------------------------------------|--------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| Microsoft.AspNetCore.Hosting          | http-server-current-requests               | Number of HTTP requests that are currently active on the server                                                           |
| Microsoft.AspNetCore.Hosting          | http-server-request-duration               | The duration of HTTP requests on the server                                                                               |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-current-connections                | Number of connections that are currently active on the server                                                             |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-connection-duration                | The duration of connections on the server                                                                                 |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-rejected-connections               | Number of connections rejected by the server                                                                              |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-queued-connections                 | Number of connections that are currently queued and are waiting to starter                                                |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-queued-requests                    | Number of HTTP requests on multiplexed connections (HTTP/2 and HTTP/3) that are currently queued and are waiting to start |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-current-upgraded-connections       | Number of HTTP connections that are currently upgraded (WebSockets). The number only tracks HTTP/1.1 connections          |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-tls-handshake-duration             | The duration of TLS handshakes on the server                                                                              |
| Microsoft.AspNetCore.Server.Kestrel   | kestrel-current-tls-handshakes             | Number of TLS handshakes that are currently in progress on the server                                                     |
| Microsoft.AspNetCore.Http.Connections | signalr-http-transport-current-connections | Number of connections that are currently active on the server                                                             |
| Microsoft.AspNetCore.Http.Connections | signalr-http-transport-current-transports  | Number of connection transports that are currently active on the server                                                   |

### OpenTelemetry.Instrumentation.AspNetCore

[Link to the repository](https://github.com/open-telemetry/opentelemetry-dotnet/tree/main/src/OpenTelemetry.Instrumentation.AspNetCore).

| Metric               | Description                                    |
|----------------------|------------------------------------------------|
| http.server.duration | Measures the duration of inbound HTTP requests |

### OpenTelemetry.Instrumentation.Http

[Link to the repository](https://github.com/open-telemetry/opentelemetry-dotnet/tree/main/src/OpenTelemetry.Instrumentation.Http)

| Metric               | Description                                     |
|----------------------|-------------------------------------------------|
| http.client.duration | Measures the duration of outbound HTTP requests |

### OpenTelemetry.Instrumentation.Runtime

[Link to the repository](https://github.com/open-telemetry/opentelemetry-dotnet-contrib/tree/main/src/OpenTelemetry.Instrumentation.Runtime)

| Metric                                                   | Description                                                                                                      |
|----------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| process.runtime.dotnet.gc.collections.count              | Number of garbage collections that have occurred since process start                                             |
| process.runtime.dotnet.gc.objects.size                   | Count of bytes currently in use by objects in the GC heap that haven't been collected yet                        |
| process.runtime.dotnet.gc.allocations.size               | Count of bytes allocated on the managed GC heap since the process start                                          |
| process.runtime.dotnet.gc.committed_memory.size          | The amount of committed virtual memory for the managed GC heap, as observed during the latest garbage collection |
| process.runtime.dotnet.gc.heap.size                      | The heap size (including fragmentation), as observed during the latest garbage collection                        |
| process.runtime.dotnet.gc.heap.fragmentation.size        | The heap fragmentation, as observed during the latest garbage collection                                         |
| process.runtime.dotnet.gc.duration                       | The total amount of time paused in GC since the process start                                                    |
| process.runtime.dotnet.jit.il_compiled.size              | Count of bytes of intermediate language that have been compiled since the process start                          |
| process.runtime.dotnet.jit.methods_compiled.count        | The number of times the JIT compiler compiled a method since the process start                                   |
| process.runtime.dotnet.jit.compilation_time              | The amount of time the JIT compiler has spent compiling methods since the process start                          |
| process.runtime.dotnet.monitor.lock_contention.count     | The number of times there was contention when trying to acquire a monitor lock since the process start           |
| process.runtime.dotnet.thread_pool.threads.count         | The number of thread pool threads that currently exist                                                           |
| process.runtime.dotnet.thread_pool.completed_items.count | The number of work items that have been processed by the thread pool since the process start                     |
| process.runtime.dotnet.thread_pool.queue.length          | The number of work items that are currently queued to be processed by the thread pool                            |
| process.runtime.dotnet.timer.count                       | The number of timer instances that are currently active                                                          |
| process.runtime.dotnet.assemblies.count                  | The number of .NET assemblies that are currently loaded                                                          |
| process.runtime.dotnet.exceptions.count                  | Count of exceptions that have been thrown in managed code, since the observation started                         |

### OpenTelemetry.Instrumentation.Process

[Link to the repository](https://github.com/open-telemetry/opentelemetry-dotnet-contrib/tree/main/src/OpenTelemetry.Instrumentation.Process)

| Metric                 | Description                                                           |
|------------------------|-----------------------------------------------------------------------|
| process.memory.usage   | The amount of physical memory allocated for this process              |
| process.memory.virtual | The amount of committed virtual memory for this process               |
| process.cpu.time       | Total CPU seconds broken down by states                               |
| process.cpu.count      | The number of processors (CPU cores) available to the current process |
| process.threads        | Process threads count                                                 |

### OpenTelemetry.Instrumentation.EventCounters

[Link to the repository](https://github.com/open-telemetry/opentelemetry-dotnet-contrib/tree/main/src/OpenTelemetry.Instrumentation.EventCounters)

It's an interesting instrumentation library that enables you to publish your `EventCounters` as OpenTelemetry metrics.

<seealso>
  <category ref="ext">
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/metrics">.NET metrics</a>
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/observability-with-otel">.NET observability with OpenTelemetry</a>
    <a href="https://github.com/open-telemetry/opentelemetry-dotnet">OpenTelemetry .NET</a>
    <a href="https://github.com/open-telemetry/opentelemetry-dotnet-contrib">OpenTelemetry .NET Contrib</a>
  </category>
</seealso>