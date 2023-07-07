package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess

data class MemoryDumpModel(
    var selectedProcess: DotNetProcess?,
    var type: DumpType,
    var path: String,
    var filename: String,
    var diag: Boolean
)

interface LiveSessionModel {
    var selectedProcess: DotNetProcess?
    var stoppingType: StoppingType
    var duration: Int
}

interface ExportSessionModel {
    var selectedProcess: DotNetProcess?
    var path: String
    var filename: String
}

data class CounterSessionModel(
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
) : LiveSessionModel, ExportSessionModel

data class GcEventSessionModel(
    override var selectedProcess: DotNetProcess?,
    override var path: String,
    override var filename: String,
    override var stoppingType: StoppingType,
    override var duration: Int
) : LiveSessionModel, ExportSessionModel

data class TraceSessionModel(
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
) : LiveSessionModel, ExportSessionModel

data class ChartSessionModel(
    override var selectedProcess: DotNetProcess?,
    override var stoppingType: StoppingType,
    override var duration: Int
) : LiveSessionModel

data class MonitoringTimerModel(var stoppingType: StoppingType, var duration: Int)
