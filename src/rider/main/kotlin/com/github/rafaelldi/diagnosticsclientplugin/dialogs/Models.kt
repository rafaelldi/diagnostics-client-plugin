package com.github.rafaelldi.diagnosticsclientplugin.dialogs

data class CollectDumpModel(var type: DumpType, var path: String, var filename: String, var diag: Boolean)

data class MonitorCountersModel(
    var interval: Int,
    var stoppingType: StoppingType,
    var duration: Int,
    var providers: String,
    var metrics: String,
    var maxTimeSeries: Int,
    var maxHistograms: Int
)

data class CollectCountersModel(
    var path: String,
    var filename: String,
    var format: CounterFileFormat,
    var interval: Int,
    var stoppingType: StoppingType,
    var duration: Int,
    var providers: String,
    var metrics: String,
    var maxTimeSeries: Int,
    var maxHistograms: Int
)

data class MonitorGcEventsModel(var stoppingType: StoppingType, var duration: Int)

data class CollectGcEventsModel(
    var path: String,
    var filename: String,
    var stoppingType: StoppingType,
    var duration: Int
)

data class CollectTracesModel(
    var path: String,
    var filename: String,
    var stoppingType: StoppingType,
    var duration: Int,
    var profile: TracingProfile,
    var providers: String
)

data class MonitoringTimerModel(var stoppingType: StoppingType, var duration: Int)
