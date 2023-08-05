package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath

@Service(Service.Level.PROJECT)
@State(name = "CounterSettings", storages = [(Storage("diagnostics-client.xml"))])
class CounterSettings(project: Project) : SimplePersistentStateComponent<CounterSettings.CounterSettingsState>(
    CounterSettingsState(project.solutionDirectoryPath.toString())
) {
    companion object {
        fun getInstance(project: Project): CounterSettings = project.service()
    }

    fun getModel(selected: DotNetProcess) = CounterSessionModel(
        selected,
        state.path ?: "",
        state.filename ?: "",
        state.format,
        state.interval,
        state.stoppingType,
        state.duration,
        state.providers ?: "",
        state.metrics ?: "",
        state.maxTimeSeries,
        state.maxHistograms
    )

    fun update(model: CounterSessionModel, persistent: Boolean) {
        state.apply {
            if (persistent) {
                path = model.path
                filename = model.filename
                format = model.format
            }
            interval = model.interval
            stoppingType = model.stoppingType
            duration = model.duration
            providers = model.providers
            metrics = model.metrics
            maxTimeSeries = model.maxTimeSeries
            maxHistograms = model.maxHistograms
        }
    }

    class CounterSettingsState(solutionPath: String = "") : BaseState() {
        var path by string(solutionPath)
        var filename by string("counters")
        var format by enum(CounterFileFormat.Csv)
        var interval by property(1)
        var stoppingType by enum(StoppingType.AfterPeriod)
        var duration by property(30)
        var providers by string("System.Runtime")
        var metrics by string("")
        var maxTimeSeries by property(1000)
        var maxHistograms by property(10)
    }
}
