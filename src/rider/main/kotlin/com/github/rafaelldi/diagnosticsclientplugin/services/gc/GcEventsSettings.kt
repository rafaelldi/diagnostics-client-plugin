package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath

@Service
@State(name = "GcEventsSettings", storages = [(Storage("diagnostics-client.xml"))])
class GcEventsSettings(project: Project) : SimplePersistentStateComponent<GcEventsSettings.GcSettingsState>(
    GcSettingsState(project.solutionDirectoryPath.toString())
) {
    companion object {
        fun getInstance(project: Project): GcEventsSettings = project.service()
    }

    fun getMonitorModel() = MonitorGcEventsModel(state.stoppingType, state.duration)

    fun getCollectModel() = CollectGcEventsModel(
        state.path ?: "",
        state.filename ?: "",
        state.stoppingType,
        state.duration
    )

    fun update(model: MonitorGcEventsModel) {
        state.apply {
            stoppingType = model.stoppingType
            duration = model.duration
        }
    }

    fun update(model: CollectGcEventsModel) {
        state.apply {
            path = model.path
            filename = model.filename
            stoppingType = model.stoppingType
            duration = model.duration
        }
    }

    class GcSettingsState(solutionPath: String = "") : BaseState() {
        var path by string(solutionPath)
        var filename by string("gc-events")
        var stoppingType by enum(StoppingType.AfterPeriod)
        var duration by property(30)
    }
}
