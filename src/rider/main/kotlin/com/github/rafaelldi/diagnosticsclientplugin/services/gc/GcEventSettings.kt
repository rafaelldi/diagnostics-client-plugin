package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath

@Service(Service.Level.PROJECT)
@State(name = "GcEventSettings", storages = [(Storage("diagnostics-client.xml"))])
class GcEventSettings(project: Project) : SimplePersistentStateComponent<GcEventSettings.GcEventSettingsState>(
    GcEventSettingsState(project.solutionDirectoryPath.toString())
) {
    companion object {
        fun getInstance(project: Project): GcEventSettings = project.service()
    }

    fun getModel(selected: DotNetProcess) = GcEventSessionModel(
        selected,
        state.path ?: "",
        state.filename ?: "",
        state.stoppingType,
        state.duration
    )

    fun update(model: GcEventSessionModel, persistent: Boolean) {
        state.apply {
            if (persistent) {
                path = model.path
                filename = model.filename
            }
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
