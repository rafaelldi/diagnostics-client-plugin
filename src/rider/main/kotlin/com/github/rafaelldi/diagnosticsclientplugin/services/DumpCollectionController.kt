package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectDumpModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectDumpCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.progress.withBackgroundProgressIndicator
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.projectView.solution

@Service
class DumpCollectionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    suspend fun collectDump(pid: Int, model: CollectDumpModel) {
        val command = CollectDumpCommand(pid, model.type.map(), model.path, model.filename, model.diag)
        withBackgroundProgressIndicator(project, "Collecting memory dump") {
            val result = hostModel.collectDump.startSuspending(command)
            Notification(
                "Diagnostics Client",
                "Dump collected",
                "",
                NotificationType.INFORMATION
            )
                .addAction(RevealFileAction(result.filePath))
                .notify(project)
        }
    }
}