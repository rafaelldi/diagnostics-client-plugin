package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectTracesModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TracingProfile
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@State(name = "TracesSettings", storages = [(Storage("diagnostics-client.xml"))])
class TracesSettings(project: Project) :
    SimplePersistentStateComponent<TracesSettings.TracesSettingsState>(
        TracesSettingsState(
            project.solutionDirectoryPath.toString(),
            getDefaultFilename()
        )
    ) {
    companion object {
        @JvmStatic
        fun getInstance(project: Project): TracesSettings = project.service()

        private fun getDefaultFilename(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val formatted = current.format(formatter)
            return "trace_${formatted}.nettrace"
        }
    }

    fun getModel() = CollectTracesModel(
        state.path ?: "",
        state.filename ?: "",
        state.stoppingType,
        state.duration,
        state.profile,
        state.providers ?: ""
    )

    fun update(model: CollectTracesModel) {
        state.apply {
            path = model.path
            filename = model.filename
            stoppingType = model.stoppingType
            duration = model.duration
            profile = model.profile
            providers = model.providers
        }
    }

    class TracesSettingsState(solutionPath: String?, defaultFilename: String?) : BaseState() {
        var path by string(solutionPath)
        var filename by string(defaultFilename)
        var stoppingType by enum(StoppingType.AfterPeriod)
        var duration by property(30)
        var profile by enum(TracingProfile.CpuSampling)
        var providers by string("")
    }
}
