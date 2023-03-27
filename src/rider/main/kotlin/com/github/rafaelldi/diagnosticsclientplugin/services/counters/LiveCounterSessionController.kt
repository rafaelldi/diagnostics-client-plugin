package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.liveSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class LiveCounterSessionController(project: Project) :
    LiveSessionController<LiveCounterSession, CounterModel>(project) {
    companion object {
        fun getInstance(project: Project): LiveCounterSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val sessions = project.solution.diagnosticsHostModel.liveCounterSessions

    init {
        sessions.view(serviceLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: CounterModel): LiveCounterSession {
        val metrics = model.metrics.ifEmpty { null }
        return LiveCounterSession(
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
        )
    }

    override fun sessionNotFound(pid: Int) = liveSessionNotFound(COUNTERS, pid, project)
    override fun sessionStarted(pid: Int) = liveSessionStarted(COUNTERS, pid, project)
    override fun sessionFinished(pid: Int) = liveSessionFinished(COUNTERS, pid, project)
}