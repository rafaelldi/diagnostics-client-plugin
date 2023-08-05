@file:Suppress("BooleanLiteralArgument")

package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TracingProfile
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service(Service.Level.PROJECT)
@State(name = "TraceSettings", storages = [(Storage("diagnostics-client.xml"))])
class TraceSettings(project: Project) : SimplePersistentStateComponent<TraceSettings.TraceSettingsState>(
    TraceSettingsState(project.solutionDirectoryPath.toString(), getDefaultFilename())
) {
    companion object {
        fun getInstance(project: Project): TraceSettings = project.service()

        private fun getDefaultFilename(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val formatted = current.format(formatter)
            return "trace_${formatted}.nettrace"
        }
    }

    fun getModel(selected: DotNetProcess) = TraceSessionModel(
        selected,
        state.path ?: "",
        state.filename ?: "",
        state.stoppingType,
        state.duration,
        state.profile,
        state.providers ?: "",
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false
    )

    fun update(model: TraceSessionModel, persistent: Boolean) {
        state.apply {
            if (persistent) {
                path = model.path
                filename = model.filename
            }
            stoppingType = model.stoppingType
            duration = model.duration
            profile = model.profile
            providers = model.providers
        }
    }

    class TraceSettingsState(solutionPath: String = "", defaultFilename: String = "") : BaseState() {
        var path by string(solutionPath)
        var filename by string(defaultFilename)
        var stoppingType by enum(StoppingType.AfterPeriod)
        var duration by property(30)
        var profile by enum(TracingProfile.CpuSampling)
        var providers by string("")
    }
}
