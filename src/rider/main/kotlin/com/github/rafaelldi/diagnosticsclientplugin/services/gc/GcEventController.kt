@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.model.CollectGcEventCommand
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventCollectionResult
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.topics.ArtifactListener
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.platform.ide.progress.withBackgroundProgress
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class GcEventController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): GcEventController = project.service()
    }

    suspend fun collect(model: GcEventSessionModel) {
        val hostModel = DiagnosticsHost.getInstance(project).hostModel ?: return

        val pid = model.selectedProcess?.pid ?: return
        val filePath = Path(model.path, "${model.filename}.csv").pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        val command = CollectGcEventCommand(
            pid,
            duration,
            filePath
        )

        withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.gc.events")) {
            withUiContext {
                val result = hostModel.collectGcEvents.startSuspending(command)
                notify(result)
                project.messageBus.syncPublisher(ArtifactListener.TOPIC).artifactCreated(result.filePath)
            }
        }
    }

    private fun notify(result: GcEventCollectionResult) = Notification(
        "Diagnostics Client",
        DiagnosticsClientBundle.message("notifications.gc.events.collected"),
        "",
        NotificationType.INFORMATION
    )
        .addAction(OpenFileAction(result.filePath))
        .notify(project)
}