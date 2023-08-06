package com.github.rafaelldi.diagnosticsclientplugin.utils

private const val FILENAME_PATTERN = "^\\w(?:[\\w. -]*\\w)?(?:\\.\\w+)?\$"
private val filenameRegex = Regex(FILENAME_PATTERN)
fun isValidFilename(filename: String): Boolean = filenameRegex.matches(filename)

// Valid providers:
// System.Runtime
// System.Runtime[cpu-usage,working-set]
// System.Net.Http[http11-connections-current-total,http20-connections-current-total]
// System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting
// System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting[total-requests]
// System.Runtime [ cpu-usage , working-set ] , Microsoft.AspNetCore.Hosting [ total-requests ]
private const val COUNTER_PROVIDERS_PATTERN =
    "^(?:\\s*[\\w\\-.]+\\s*(?:\\[\\s*[\\w-]+(?:\\s*,\\s*[\\w-]+)*\\s*]\\s*)?(?:,\\s*[\\w\\-.]+\\s*(?:\\[\\s*[\\w-]+(?:\\s*,\\s*[\\w-]+)*\\s*])?\\s*)*)?\\s*\$"
private val counterProvidersRegex = Regex(COUNTER_PROVIDERS_PATTERN)
fun isValidCounterProviderList(providers: String): Boolean = counterProvidersRegex.matches(providers)

// Valid metrics:
// OpenTelemetry.Instrumentation.Runtime
// OpenTelemetry.Instrumentation.Runtime[process.cpu.count]
// OpenTelemetry.Instrumentation.Runtime[process.cpu.count,process.runtime.dotnet.gen_0-gc.count]
// OpenTelemetry.Instrumentation.Runtime[process.cpu.count],OpenTelemetry.Instrumentation.AspNetCore
// OpenTelemetry.Instrumentation.Runtime[process.cpu.count],OpenTelemetry.Instrumentation.AspNetCore[http.server.duration]
// OpenTelemetry.Instrumentation.Runtime [ process.cpu.count ] , OpenTelemetry.Instrumentation.AspNetCore [ http.server.duration ]
private const val METRIC_LIST_PATTERN =
    "^(?:\\s*[\\w\\-.]+\\s*(?:\\[\\s*[\\w-.]+(?:\\s*,\\s*[\\w-.]+)*\\s*]\\s*)?(?:,\\s*[\\w\\-.]+\\s*(?:\\[\\s*[\\w-.]+(?:\\s*,\\s*[\\w-.]+)*\\s*])?\\s*)*)?\\s*\$"
private val metricListRegex = Regex(METRIC_LIST_PATTERN)
fun isValidMetricList(providers: String): Boolean = metricListRegex.matches(providers)