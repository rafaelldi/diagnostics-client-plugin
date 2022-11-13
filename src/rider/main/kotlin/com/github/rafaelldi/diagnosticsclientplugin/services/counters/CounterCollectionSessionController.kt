package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterCollectionSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import java.time.Duration
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class CounterCollectionSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): CounterCollectionSessionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.counterCollectionSessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: CollectCountersModel) {
        if (hostModel.counterCollectionSessions.contains(pid)) {
            sessionAlreadyExists(pid)
            return
        }

        val filePath = calculateFilePath(model)
        val metrics = model.metrics.ifEmpty { null }
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        val session = CounterCollectionSession(
            filePath,
            model.format.map(),
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
            duration
        )

        try {
            hostModel.counterCollectionSessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            sessionAlreadyExists(pid)
        }
    }

    fun stopSession(pid: Int) {
        hostModel.counterCollectionSessions.remove(pid)
    }

    private fun calculateFilePath(model: CollectCountersModel): String {
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

    private fun viewSession(pid: Int, session: CounterCollectionSession, lt: Lifetime) {
        if (session.duration != null) {
            val timerLifetime =
                lt.createTerminatedAfter(Duration.ofSeconds(session.duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (hostModel.counterCollectionSessions.containsKey(pid)) {
                    hostModel.counterCollectionSessions.remove(pid)
                }
            }
        }

        lt.bracketIfAlive(
            { sessionStarted(pid) },
            { sessionFinished(pid, session.filePath) }
        )
    }

    private fun sessionAlreadyExists(pid: Int) = Notification(
        "Diagnostics Client",
        "Session for $pid already exists",
        "",
        NotificationType.WARNING
    )
        .notify(project)

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