package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service
class LiveCounterSessionController(project: Project) :
    LiveSessionController<LiveCounterSession, CounterSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): LiveCounterSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val artifactType = COUNTERS

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.liveCounterSessions

    override fun addSessionTab(pid: Int, session: LiveCounterSession, sessionLifetime: Lifetime) {
        CounterSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: CounterSessionModel): LiveCounterSession {
        val metrics = model.metrics.ifEmpty { null }
        return LiveCounterSession(
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
        )
    }
}