@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services.traces

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.CollectTraceCommand
import com.github.rafaelldi.diagnosticsclientplugin.model.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceCollectionResult
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
class TraceController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): TraceController = project.service()
    }

    suspend fun collect(model: TraceSessionModel) {
        val hostModel = DiagnosticsHost.getInstance(project).hostModel ?: return

        val pid = model.selectedProcess?.pid ?: return
        val filePath = Path(model.path, model.filename).pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null
        val predefinedProvider = getPredefinedProviders(model)

        val command = CollectTraceCommand(
            pid,
            duration,
            filePath,
            model.profile.map(),
            model.providers,
            predefinedProvider
        )

        withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.traces")) {
            withUiContext {
                val result = hostModel.collectTraces.startSuspending(command)
                notify(result)
                project.messageBus.syncPublisher(ArtifactListener.TOPIC).artifactCreated(result.filePath)
            }
        }
    }

    private fun getPredefinedProviders(model: TraceSessionModel) = buildList {
        if (model.http)
            add(PredefinedProvider.Http)
        if (model.aspNet)
            add(PredefinedProvider.AspNet)
        if (model.ef)
            add(PredefinedProvider.EF)
        if (model.exceptions)
            add(PredefinedProvider.Exceptions)
        if (model.threads)
            add(PredefinedProvider.Threads)
        if (model.contentions)
            add(PredefinedProvider.Contentions)
        if (model.tasks)
            add(PredefinedProvider.Tasks)
        if (model.loader)
            add(PredefinedProvider.Loader)
    }

    private fun notify(result: TraceCollectionResult) = Notification(
        "Diagnostics Client",
        DiagnosticsClientBundle.message("notifications.traces.collected"),
        "",
        NotificationType.INFORMATION
    )
        .addAction(RevealFileAction(result.filePath))
        .notify(project)
}