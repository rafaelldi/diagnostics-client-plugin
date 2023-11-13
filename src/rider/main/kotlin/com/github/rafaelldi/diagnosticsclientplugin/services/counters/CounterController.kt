@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.model.CollectCounterCommand
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterCollectionResult
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
class CounterController(private val project: Project) {
    companion object {
        fun getInstance(project: Project): CounterController = project.service()
    }

    suspend fun collect(model: CounterSessionModel) {
        val hostModel = DiagnosticsHost.getInstance(project).hostModel ?: return

        val pid = model.selectedProcess?.pid ?: return
        val filePath = calculateFilePath(model)
        val metrics = model.metrics.ifEmpty { null }
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        val command = CollectCounterCommand(
            pid,
            duration,
            filePath,
            model.format.map(),
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms
        )

        withBackgroundProgress(project, DiagnosticsClientBundle.message("progress.collecting.counters")) {
            withUiContext {
                val result = hostModel.collectCounters.startSuspending(command)
                notify(result)
                project.messageBus.syncPublisher(ArtifactListener.TOPIC).artifactCreated(result.filePath)
            }
        }
    }

    private fun calculateFilePath(model: CounterSessionModel): String {
        val filename = when (model.format) {
            CounterFileFormat.Csv -> {
                if (model.filename.endsWith(".csv"))
                    model.filename
                else
                    "${model.filename}.csv"
            }

            CounterFileFormat.Json -> {
                if (model.filename.endsWith(".json"))
                    model.filename
                else
                    "${model.filename}.json"
            }
        }

        return Path(model.path, filename).pathString
    }

    private fun notify(result: CounterCollectionResult) = Notification(
        "Diagnostics Client",
        DiagnosticsClientBundle.message("notifications.counters.collected"),
        "",
        NotificationType.INFORMATION
    )
        .addAction(OpenFileAction(result.filePath))
        .notify(project)
}