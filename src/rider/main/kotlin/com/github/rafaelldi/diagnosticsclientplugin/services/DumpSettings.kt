package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectDumpModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.DumpType
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.jetbrains.rider.projectView.solutionDirectoryPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@State(name = "DumpSettings", storages = [(Storage("diagnostics-client.xml"))])
class DumpSettings(project: Project) :
    SimplePersistentStateComponent<DumpSettings.DumpSettingsState>(
        DumpSettingsState(
            project.solutionDirectoryPath.toString(),
            getDefaultFilename()
        )
    ) {
    companion object {
        fun getInstance(project: Project): DumpSettings = project.service()

        private fun getDefaultFilename(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val formatted = current.format(formatter)
            return if (SystemInfo.isWindows) {
                "dump_${formatted}.dmp"
            } else {
                "core_${formatted}"
            }
        }
    }

    fun getModel() = CollectDumpModel(
        state.type,
        state.path ?: "",
        state.filename ?: "",
        state.diag
    )

    fun update(model: CollectDumpModel) {
        state.apply {
            path = model.path
            filename = model.filename
            type = model.type
            diag = model.diag
        }
    }

    class DumpSettingsState(solutionPath: String = "", defaultFilename: String = "") : BaseState() {
        var path by string(solutionPath)
        var filename by string(defaultFilename)
        var type by enum(DumpType.Full)
        var diag by property(false)
    }
}
