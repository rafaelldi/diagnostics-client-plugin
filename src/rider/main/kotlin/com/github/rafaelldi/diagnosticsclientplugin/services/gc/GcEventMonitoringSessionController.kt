package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventMonitoringSession
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
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import java.time.Duration

@Service
class GcEventMonitoringSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): GcEventMonitoringSessionController = project.service()
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.gcEventMonitoringSessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: MonitorGcEventsModel) {
        if (!hostModel.gcEventMonitoringSessions.contains(pid)) {
            createNewSession(pid)
        }

        startExistingSession(pid, model.stoppingType, model.duration)
    }

    private fun createNewSession(pid: Int) {
        if (hostModel.gcEventMonitoringSessions.contains(pid)) {
            return
        }

        val session = GcEventMonitoringSession()

        try {
            hostModel.gcEventMonitoringSessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            // do nothing
        }
    }

    fun startExistingSession(pid: Int, stoppingType: StoppingType, duration: Int) {
        val session = hostModel.gcEventMonitoringSessions[pid]
        if (session == null) {
            sessionNotFound(pid)
            return
        }

        if (session.active.valueOrNull == true) {
            return
        }

        if (stoppingType == StoppingType.AfterPeriod) {
            session.duration.set(duration)
        } else {
            session.duration.set(null)
        }

        session.active.set(true)
    }

    fun stopSession(pid: Int) {
        val session = hostModel.gcEventMonitoringSessions[pid]
        if (session == null) {
            sessionNotFound(pid)
            return
        }

        if (session.active.valueOrNull == true) {
            session.active.set(false)
        }
    }

    fun closeSession(pid: Int) {
        hostModel.gcEventMonitoringSessions.remove(pid)
    }

    private fun viewSession(pid: Int, session: GcEventMonitoringSession, lifetime: Lifetime) {
        session.active.whenTrue(lifetime) { lt -> viewActiveStatus(pid, session, lt) }
    }

    private fun viewActiveStatus(pid: Int, session: GcEventMonitoringSession, lifetime: Lifetime) {
        val duration = session.duration.value
        if (duration != null) {
            val timerLifetime =
                lifetime.createTerminatedAfter(Duration.ofSeconds(duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (session.active.valueOrNull == true) {
                    session.active.set(false)
                }
            }
        }

        lifetime.bracketIfAlive(
            { sessionStarted(pid) },
            { sessionFinished(pid) }
        )
    }

    private fun sessionStarted(pid: Int) = Notification(
        "Diagnostics Client",
        "GC monitoring started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int) = Notification(
        "Diagnostics Client",
        "GC monitoring finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionNotFound(pid: Int) = Notification(
        "Diagnostics Client",
        "GC events monitoring session for $pid not found",
        "",
        NotificationType.ERROR
    )
        .notify(project)
}