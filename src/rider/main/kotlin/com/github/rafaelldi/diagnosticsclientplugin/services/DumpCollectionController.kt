@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectDumpModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectDumpCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.withBackgroundProgress
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class DumpCollectionController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): DumpCollectionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    suspend fun collectDump(model: CollectDumpModel) {
        val pid = model.selectedProcess?.pid ?: return

        val command = CollectDumpCommand(pid, model.type.map(), model.path, model.filename, model.diag)
        withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.memory.dump")) {
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