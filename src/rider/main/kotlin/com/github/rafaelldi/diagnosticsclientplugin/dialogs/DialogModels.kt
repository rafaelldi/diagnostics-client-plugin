package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess

data class CollectDumpModel(var type: DumpType, var path: String, var filename: String, var diag: Boolean)

interface MonitoringModel {
    var selectedProcess: DotNetProcess?
    var stoppingType: StoppingType
    var duration: Int
}

interface CollectionModel {
    var selectedProcess: DotNetProcess?
}

data class MonitorCountersModel(
    override var selectedProcess: DotNetProcess?,
    var interval: Int,
    override var stoppingType: StoppingType,
    override var duration: Int,
    var providers: String,
    var metrics: String,
    var maxTimeSeries: Int,
    var maxHistograms: Int
) : MonitoringModel

data class CollectCountersModel(
    override var selectedProcess: DotNetProcess?,
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
) : CollectionModel

data class MonitorGcEventsModel(
    override var selectedProcess: DotNetProcess?,
    override var stoppingType: StoppingType,
    override var duration: Int
) : MonitoringModel

data class CollectGcEventsModel(
    override var selectedProcess: DotNetProcess?,
    var path: String,
    var filename: String,
    var stoppingType: StoppingType,
    var duration: Int
) : CollectionModel

data class MonitorTracesModel(
    override var selectedProcess: DotNetProcess?,
    override var stoppingType: StoppingType,
    override var duration: Int,
    var http: Boolean,
    var aspNet: Boolean,
    var ef: Boolean,
    var exceptions: Boolean,
    var threads: Boolean,
    var contentions: Boolean,
    var tasks: Boolean,
    var loader: Boolean
) : MonitoringModel

data class CollectTracesModel(
    override var selectedProcess: DotNetProcess?,
    var path: String,
    var filename: String,
    var stoppingType: StoppingType,
    var duration: Int,
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
) : CollectionModel

data class MonitoringTimerModel(var stoppingType: StoppingType, var duration: Int)
