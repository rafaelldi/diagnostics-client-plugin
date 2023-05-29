package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess

data class CollectDumpModel(
    var selectedProcess: DotNetProcess?,
    var type: DumpType,
    var path: String,
    var filename: String,
    var diag: Boolean
)

interface LiveModel {
    var selectedProcess: DotNetProcess?
    var stoppingType: StoppingType
    var duration: Int
}

interface PersistentModel {
    var selectedProcess: DotNetProcess?
    var path: String
    var filename: String
}

data class CounterModel(
    override var selectedProcess: DotNetProcess?,
    override var path: String,
    override var filename: String,
    var format: CounterFileFormat,
    var interval: Int,
    override var stoppingType: StoppingType,
    override var duration: Int,
    var providers: String,
    var metricsEnabled: Boolean,
    var metrics: String,
    var maxTimeSeries: Int,
    var maxHistograms: Int
) : LiveModel, PersistentModel

data class GcEventModel(
    override var selectedProcess: DotNetProcess?,
    override var path: String,
    override var filename: String,
    override var stoppingType: StoppingType,
    override var duration: Int
) : LiveModel, PersistentModel

data class TraceModel(
    override var selectedProcess: DotNetProcess?,
    override var path: String,
    override var filename: String,
    override var stoppingType: StoppingType,
    override var duration: Int,
    var profile: TracingProfile,
    var providers: String,
    var http: Boolean,
    var aspNet: Boolean,
    var ef: Boolean,
    var exceptions: Boolean,
    var threads: Boolean,
    var contentions: Boolean,
    var tasks: Boolean,
    var loader: Boolean
) : LiveModel, PersistentModel

data class ChartModel(
    override var selectedProcess: DotNetProcess?,
    override var stoppingType: StoppingType,
    override var duration: Int
) : LiveModel

data class MonitoringTimerModel(var stoppingType: StoppingType, var duration: Int)
