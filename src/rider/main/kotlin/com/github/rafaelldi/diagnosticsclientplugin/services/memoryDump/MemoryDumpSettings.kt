package com.github.rafaelldi.diagnosticsclientplugin.services.memoryDump

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.DumpType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MemoryDumpModel
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.jetbrains.rider.projectView.solutionDirectoryPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service(Service.Level.PROJECT)
@State(name = "DumpSettings", storages = [(Storage("diagnostics-client.xml"))])
class MemoryDumpSettings(project: Project) :
    SimplePersistentStateComponent<MemoryDumpSettings.DumpSettingsState>(
        DumpSettingsState(
            project.solutionDirectoryPath.toString(),
            getDefaultFilename()
        )
    ) {
    companion object {
        fun getInstance(project: Project): MemoryDumpSettings = project.service()

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

    fun getModel(pid: Int) = MemoryDumpModel(
        pid,
        state.type,
        state.path ?: "",
        state.filename ?: "",
        state.diag
    )

    fun update(model: MemoryDumpModel) {
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
