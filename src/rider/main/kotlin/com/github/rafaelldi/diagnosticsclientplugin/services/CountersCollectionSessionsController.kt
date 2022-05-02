package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CountersFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.*
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class CountersCollectionSessionsController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.countersCollectionSessions.view(projectComponentLifetime) { lt, _, session ->
            viewSession(lt, session)
        }
    }

    fun startSession(pid: Int, model: CollectCountersModel) {
        val filepath = calculateFilePath(model)
        val duration = if (model.stoppingType == StoppingType.AfterPeriod) model.duration else null
        val command = CollectCountersCommand(
            pid,
            filepath,
            model.format.map(),
            model.interval,
            model.providers,
            duration
        )
        hostModel.collectCounters.start(projectComponentLifetime, command)
            .result
            .advise(projectComponentLifetime) { result -> result.unwrap() }
    }

    private fun calculateFilePath(model: CollectCountersModel): String {
        val filename = when (model.format) {
            CountersFileFormat.Csv -> {
                if (model.filename.endsWith(".csv"))
                    model.filename
                else
                    "${model.filename}.csv"
            }
            CountersFileFormat.Json -> {
                if (model.filename.endsWith(".json"))
                    model.filename
                else
                    "${model.filename}.json"
            }
        }

        return Path(model.path, filename).pathString
    }

    fun stopSession(pid: Int) {
        hostModel.stopCountersCollection.fire(StopCountersCollectionCommand(pid))
    }

    private fun viewSession(lt: Lifetime, session: CountersCollectionSession) {
        lt.bracket(
            { sessionStarted(session.pid) },
            { sessionFinished(session.pid, session.filePath) }
        )
    }

    private fun sessionStarted(pid: Int) = Notification(
        "Diagnostics Client",
        "Counters collection started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int, filePath: String) = Notification(
        "Diagnostics Client",
        "Counters collection finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(OpenFileAction(filePath))
        .notify(project)
}