@file:Suppress("BooleanLiteralArgument")

package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.*
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
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

    fun getCollectModel(selected: DotNetProcess) = CollectTracesModel(
        SourceProcessType.Attach,
        selected,
        "",
        "",
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

    fun getMonitorModel(selected: DotNetProcess) = MonitorTracesModel(
        SourceProcessType.Attach,
        selected,
        "",
        "",
        state.stoppingType,
        state.duration,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false
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

    fun update(model: MonitorTracesModel) {
        state.apply {
            stoppingType = model.stoppingType
            duration = model.duration
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
