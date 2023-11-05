@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services.memoryDump

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MemoryDumpModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.CollectDumpCommand
import com.github.rafaelldi.diagnosticsclientplugin.model.DumpCollectionResult
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.topics.ArtifactListener
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.platform.ide.progress.withBackgroundProgress

@Service(Service.Level.PROJECT)
class MemoryDumpController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): MemoryDumpController = project.service()
    }

    suspend fun collect(model: MemoryDumpModel) {
        val hostModel = DiagnosticsHost.getInstance(project).hostModel ?: return

        val command = CollectDumpCommand(model.pid, model.type.map(), model.path, model.filename, model.diag)
        withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.memory.dump")) {
            withUiContext {
                val result = hostModel.collectDump.startSuspending(command)
                notify(result)
                project.messageBus.syncPublisher(ArtifactListener.TOPIC).artifactCreated(result.filePath)
            }
        }
    }

    private fun notify(result: DumpCollectionResult) = Notification(
        "Diagnostics Client",
        DiagnosticsClientBundle.message("notifications.dump.collected"),
        "",
        NotificationType.INFORMATION
    )
        .addAction(RevealFileAction(result.filePath))
        .notify(project)
}