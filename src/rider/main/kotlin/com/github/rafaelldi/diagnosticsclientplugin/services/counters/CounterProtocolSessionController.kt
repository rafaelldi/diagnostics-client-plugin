package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service(Service.Level.PROJECT)
class CounterProtocolSessionController(project: Project) :
    ProtocolSessionController<CounterProtocolSession, CounterSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): CounterProtocolSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val artifactType = COUNTERS

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.counterProtocolSessions

    override fun addSessionTab(pid: Int, session: CounterProtocolSession, sessionLifetime: Lifetime) {
        CounterSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: CounterSessionModel): CounterProtocolSession {
        val metrics = model.metrics.ifEmpty { null }
        return CounterProtocolSession(
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
        )
    }
}