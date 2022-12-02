package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
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
class CounterMonitoringSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): CounterMonitoringSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.counterMonitoringSessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: MonitorCountersModel) {
        if (!hostModel.counterMonitoringSessions.contains(pid)) {
            createNewSession(pid, model)
        }

        startExistingSession(pid, model.stoppingType, model.duration)
    }

    private fun createNewSession(pid: Int, model: MonitorCountersModel) {
        if (hostModel.counterMonitoringSessions.contains(pid)) {
            return
        }

        val metrics = model.metrics.ifEmpty { null }

        val session = CounterMonitoringSession(
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
        )

        try {
            hostModel.counterMonitoringSessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            // do nothing
        }
    }

    fun startExistingSession(pid: Int, stoppingType: StoppingType, duration: Int) {
        val session = hostModel.counterMonitoringSessions[pid]
        if (session == null) {
            monitoringSessionNotFound(COUNTERS, pid, project)
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
        val session = hostModel.counterMonitoringSessions[pid]
        if (session == null) {
            monitoringSessionNotFound(COUNTERS, pid, project)
            return
        }

        if (session.active.valueOrNull == true) {
            session.active.set(false)
        }
    }

    fun closeSession(pid: Int) {
        hostModel.counterMonitoringSessions.remove(pid)
    }

    private fun viewSession(pid: Int, session: CounterMonitoringSession, lifetime: Lifetime) {
        session.active.whenTrue(lifetime) { lt -> viewActiveStatus(pid, session, lt) }
    }

    private fun viewActiveStatus(pid: Int, session: CounterMonitoringSession, lifetime: Lifetime) {
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
            { monitoringSessionStarted(COUNTERS, pid, project) },
            { monitoringSessionFinished(COUNTERS, pid, project) }
        )
    }
}