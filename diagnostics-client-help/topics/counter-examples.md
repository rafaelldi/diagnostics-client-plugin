# Counter examples

This page shows some built-in `EventCounters` that may be useful for monitoring the performance of your application.

## How to specify providers and counters

Select one or more providers and their counters and put them in the `Providers` field in comma separated format. If you
want to watch all counters of one provider, do not specify square brackets. For
example, `System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting`
or `System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting[total-requests]`.

![Monitor counters dialog with list of counter providers](counters-providers.png){ width="700" border-effect=rounded }

## System.Runtime

| Counter                          | Description                                                                                         |
|----------------------------------|-----------------------------------------------------------------------------------------------------|
| cpu-usage                        | The percent of the process's CPU usage relative to all of the system CPU resources                  |
| working-set                      | Amount of working set used by the process                                                           |
| gc-heap-size                     | Total heap size reported by the GC                                                                  |
| gen-0-gc-count                   | Number of Gen 0 GCs between update intervals                                                        |
| gen-1-gc-count                   | Number of Gen 1 GCs between update intervals                                                        |
| gen-2-gc-count                   | Number of Gen 2 GCs between update intervals                                                        |
| time-in-gc                       | % time in GC since the last GC                                                                      |
| gen-0-size                       | Gen 0 Heap Size                                                                                     |
| gen-1-size                       | Gen 1 Heap Size                                                                                     |
| gen-2-size                       | Gen 2 Heap Size                                                                                     |
| loh-size                         | LOH (Large Object Heap) Size                                                                        |
| poh-size                         | POH (Pinned Object Heap) Size                                                                       |
| alloc-rate                       | The number of bytes allocated per update interval                                                   |
| gc-fragmentation                 | GC Heap Fragmentation                                                                               |
| assembly-count                   | Number of Assemblies Loaded                                                                         |
| exception-count                  | Number of Exceptions / sec                                                                          |
| threadpool-thread-count          | Number of ThreadPool Threads                                                                        |
| monitor-lock-contention-count    | Number of times there were contention when trying to take the monitor lock between update intervals |
| threadpool-queue-length          | ThreadPool Work Items Queue Length                                                                  |
| threadpool-completed-items-count | ThreadPool Completed Work Items Count                                                               |
| active-timer-count               | Number of timers that are currently active                                                          |
| il-bytes-jitted                  | Total IL bytes jitted                                                                               |
| methods-jitted-count             | Number of methods jitted                                                                            |
| gc-committed                     | The number of bytes committed by the GC                                                             |

## Microsoft.AspNetCore.Hosting

| Counter             | Description                                 |
|---------------------|---------------------------------------------|
| requests-per-second | Number of requests between update intervals |
| total-requests      | Total number of requests                    |
| current-requests    | Current number of requests                  |
| failed-requests     | Failed number of requests                   |

## Microsoft.AspNetCore.Http.Connections

| Counter               | Description                                                             |
|-----------------------|-------------------------------------------------------------------------|
| connections-duration  | The average duration of a connection in milliseconds                    |
| current-connections   | The number of active connections that have started, but not yet stopped |
| connections-started   | The total number of connections that have started                       |
| connections-stopped   | The total number of connections that have stopped                       |
| connections-timed-out | The total number of connections that have timed out                     |

## Microsoft-AspNetCore-Server-Kestrel

| Counter                   | Description                                            |
|---------------------------|--------------------------------------------------------|
| connections-per-second    | Number of connections between update intervals         |
| total-connections         | Total Connections                                      |
| tls-handshakes-per-second | Number of TLS Handshakes made between update intervals |
| total-tls-handshakes      | Total number of TLS handshakes made                    |
| current-tls-handshakes    | Number of currently active TLS handshakes              |
| failed-tls-handshakes     | Total number of failed TLS handshakes                  |
| current-connections       | Number of current connections                          |
| connection-queue-length   | Length of Kestrel Connection Queue                     |
| request-queue-length      | Length total HTTP request queue                        |
| current-upgraded-requests | The current number of upgraded requests (WebSockets)   |

## System.Net.Http

| Counter                          | Description                                                                                  |
|----------------------------------|----------------------------------------------------------------------------------------------|
| requests-started                 | Total Requests Started                                                                       |
| requests-started-rate            | Number of Requests Started between update intervals                                          |
| requests-aborted                 | Total Requests Aborted                                                                       |
| requests-aborted-rate            | Number of Requests Aborted between update intervals                                          |
| requests-failed                  | Total Requests Failed                                                                        |
| requests-failed-rate             | Number of Requests Failed between update intervals                                           |
| current-requests                 | Current Requests                                                                             |
| http11-connections-current-total | The current number of HTTP 1.1 connections that have started but not yet completed or failed |
| http20-connections-current-total | The current number of HTTP 2.0 connections that have started but not yet completed or failed |
| http30-connections-current-total | The current number of HTTP 3.0 connections that have started but not yet completed or failed |
| http11-requests-queue-duration   | The average duration of the time HTTP 1.1 requests spent in the request queue                |
| http20-requests-queue-duration   | The average duration of the time HTTP 2.0 requests spent in the request queue                |
| http30-requests-queue-duration   | The average duration of the time HTTP 3.0 requests spent in the request queue                |

## System.Net.NameResolution

| Counter               | Description                                                                         |
|-----------------------|-------------------------------------------------------------------------------------|
| dns-lookups-requested | The number of DNS lookups requested since the process started                       |
| dns-lookups-duration  | Average DNS Lookup Duration                                                         |
| current-dns-lookups   | The current number of DNS lookups that have started but not yet completed or failed |

## System.Net.Security

| Counter                    | Description                                                                  |
|----------------------------|------------------------------------------------------------------------------|
| tls-handshake-rate         | The number of TLS handshakes completed per update interval                   |
| total-tls-handshakes       | The total number of TLS handshakes completed since the process started       |
| current-tls-handshakes     | The current number of TLS handshakes that have started but not yet completed |
| failed-tls-handshakes      | The total number of TLS handshakes failed since the process started          |
| all-tls-sessions-open      | The number of active all TLS sessions                                        |
| tls10-sessions-open        | The number of active TLS 1.0 sessions                                        |
| tls11-sessions-open        | The number of active TLS 1.1 sessions                                        |
| tls12-sessions-open        | The number of active TLS 1.2 sessions                                        |
| tls13-sessions-open        | The number of active TLS 1.3 sessions                                        |
| all-tls-handshake-duration | The average duration of all TLS handshakes                                   |
| tls10-handshake-duration   | The average duration of TLS 1.0 handshakes                                   |
| tls11-handshake-duration   | The average duration of TLS 1.1 handshakes                                   |
| tls12-handshake-duration   | The average duration of TLS 1.2 handshakes                                   |
| tls13-handshake-duration   | The average duration of TLS 1.3 handshakes                                   |

## System.Net.Sockets

| Counter                          | Description                                                                    |
|----------------------------------|--------------------------------------------------------------------------------|
| outgoing-connections-established | The total number of outgoing connections established since the process started |
| incoming-connections-established | The total number of incoming connections established since the process started |
| bytes-received                   | The total number of bytes received since the process started                   |
| bytes-sent                       | The total number of bytes sent since the process started                       |
| datagrams-received               | The total number of datagrams received since the process started               |
| datagrams-sent                   | The total number of datagrams sent since the process started                   |

## Microsoft.EntityFrameworkCore

| Counter                                          | Description                                                                                                                                                                 |
|--------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| active-db-contexts                               | The number of active, undisposed DbContext instances currently in your application                                                                                          |
| total-execution-strategy-operation-failures      | The total number of times a database operation failed to execute                                                                                                            |
| execution-strategy-operation-failures-per-second | The number of times a database operation failed to execute per update interval                                                                                              |
| total-optimistic-concurrency-failures            | The total number of times SaveChanges failed because of an optimistic concurrency error, because data in the data store was changed since your code loaded it               |
| optimistic-concurrency-failures-per-second       | The number of times SaveChanges failed because of an optimistic concurrency error, because data in the data store was changed since your code loaded it per update interval |
| total-queries                                    | The total number of queries executed                                                                                                                                        |
| queries-per-second                               | The number of queries executed per update interval                                                                                                                          |
| compiled-query-cache-hit-rate                    | The ratio of query cache hits to misses                                                                                                                                     |
| total-save-changes                               | The total number of times SaveChanges has been called                                                                                                                       |
| save-changes-per-second                          | The number of times SaveChanges has been called per update interval                                                                                                         |

<seealso>
  <category ref="ext">
    <a href="https://learn.microsoft.com/en-us/dotnet/core/diagnostics/available-counters">Well-known EventCounters in .NET</a>
    <a href="https://learn.microsoft.com/en-us/ef/core/logging-events-diagnostics/event-counters">EF Core Event Counters</a>
  </category>
</seealso>