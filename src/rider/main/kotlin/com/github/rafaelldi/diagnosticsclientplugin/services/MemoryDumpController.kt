@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MemoryDumpModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.CollectDumpCommand
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.withBackgroundProgress
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext

@Service(Service.Level.PROJECT)
class MemoryDumpController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): MemoryDumpController = project.service()
    }

    suspend fun collect(model: MemoryDumpModel) {
        val pid = model.selectedProcess?.pid ?: return
        val hostModel = DiagnosticsHost.getInstance(project).hostModel ?: return

        val command = CollectDumpCommand(pid, model.type.map(), model.path, model.filename, model.diag)
        withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.memory.dump")) {
            withUiContext {
                val result = hostModel.collectDump.startSuspending(command)
                Notification(
                    "Diagnostics Client",
                    DiagnosticsClientBundle.message("notifications.dump.collected"),
                    "",
                    NotificationType.INFORMATION
                )
                    .addAction(RevealFileAction(result.filePath))
                    .notify(project)
            }
        }
    }
}