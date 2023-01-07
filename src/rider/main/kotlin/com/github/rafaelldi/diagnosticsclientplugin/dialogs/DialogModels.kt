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
    var sourceProcessType: SourceProcessType
    var selectedProcess: DotNetProcess?
    var executablePath: String
    var executableArgs: String
    var stoppingType: StoppingType
    var duration: Int
}

interface PersistentModel {
    var sourceProcessType: SourceProcessType
    var selectedProcess: DotNetProcess?
    var executablePath: String
    var executableArgs: String
    var path: String
    var filename: String
}

data class CounterModel(
    override var sourceProcessType: SourceProcessType,
    override var selectedProcess: DotNetProcess?,
    override var executablePath: String,
    override var executableArgs: String,
    override var path: String,
    override var filename: String,
    var format: CounterFileFormat,
    var interval: Int,
    override var stoppingType: StoppingType,
    override var duration: Int,
    var providers: String,
    var metrics: String,
    var maxTimeSeries: Int,
    var maxHistograms: Int
) : LiveModel, PersistentModel

data class GcEventModel(
    override var sourceProcessType: SourceProcessType,
    override var selectedProcess: DotNetProcess?,
    override var executablePath: String,
    override var executableArgs: String,
    override var path: String,
    override var filename: String,
    override var stoppingType: StoppingType,
    override var duration: Int
) : LiveModel, PersistentModel

data class TraceModel(
    override var sourceProcessType: SourceProcessType,
    override var selectedProcess: DotNetProcess?,
    override var executablePath: String,
    override var executableArgs: String,
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

data class MonitoringTimerModel(var stoppingType: StoppingType, var duration: Int)
