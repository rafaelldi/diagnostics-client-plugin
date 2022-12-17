package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.MonitoringSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class CounterMonitoringSessionController(project: Project) :
    MonitoringSessionController<CounterMonitoringSession, MonitorCountersModel>(project) {
    companion object {
        fun getInstance(project: Project): CounterMonitoringSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val sessions = project.solution.diagnosticsHostModel.counterMonitoringSessions

    init {
        sessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: MonitorCountersModel): CounterMonitoringSession {
        val metrics = model.metrics.ifEmpty { null }
        return CounterMonitoringSession(
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
        )
    }

    override fun sessionNotFound(pid: Int) = monitoringSessionNotFound(COUNTERS, pid, project)
    override fun sessionStarted(pid: Int) =  monitoringSessionStarted(COUNTERS, pid, project)
    override fun sessionFinished(pid: Int) = monitoringSessionFinished(COUNTERS, pid, project)
}