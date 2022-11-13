package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventCollectionSession
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
class GcEventCollectionSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): GcEventCollectionSessionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.gcEventCollectionSessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: CollectGcEventsModel) {
        if (hostModel.gcEventCollectionSessions.contains(pid)) {
            sessionAlreadyExists(pid)
            return
        }

        val filePath = Path(model.path, "${model.filename}.csv").pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        val session = GcEventCollectionSession(filePath, duration)

        try {
            hostModel.gcEventCollectionSessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            sessionAlreadyExists(pid)
        }
    }

    fun stopSession(pid: Int) {
        hostModel.gcEventCollectionSessions.remove(pid)
    }

    private fun viewSession(pid: Int, session: GcEventCollectionSession, lt: Lifetime) {
        if (session.duration != null) {
            val timerLifetime =
                lt.createTerminatedAfter(Duration.ofSeconds(session.duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (hostModel.gcEventCollectionSessions.containsKey(pid)) {
                    hostModel.gcEventCollectionSessions.remove(pid)
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
        "GC events collection started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int, filePath: String) = Notification(
        "Diagnostics Client",
        "GC events collection finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(OpenFileAction(filePath))
        .notify(project)
}