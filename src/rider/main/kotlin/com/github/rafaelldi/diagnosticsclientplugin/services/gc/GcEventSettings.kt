package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath

@Service
@State(name = "GcEventSettings", storages = [(Storage("diagnostics-client.xml"))])
class GcEventSettings(project: Project) : SimplePersistentStateComponent<GcEventSettings.GcEventSettingsState>(
    GcEventSettingsState(project.solutionDirectoryPath.toString())
) {
    companion object {
        fun getInstance(project: Project): GcEventSettings = project.service()
    }

    fun getMonitorModel(selected: DotNetProcess) = MonitorGcEventsModel(
        selected,
        state.stoppingType,
        state.duration
    )

    fun getCollectModel(selected: DotNetProcess) = CollectGcEventsModel(
        selected,
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

    class GcEventSettingsState(solutionPath: String = "") : BaseState() {
        var path by string(solutionPath)
        var filename by string("gc-events")
        var stoppingType by enum(StoppingType.AfterPeriod)
        var duration by property(30)
    }
}
