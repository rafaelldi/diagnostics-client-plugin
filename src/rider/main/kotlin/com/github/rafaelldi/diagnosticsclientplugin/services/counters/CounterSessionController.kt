package com.github.rafaelldi.diagnosticsclientplugin.services.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.SessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service(Service.Level.PROJECT)
class CounterSessionController(project: Project) : SessionController<CounterSession, CounterSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): CounterSessionController = project.service()
        private const val COUNTERS = "Counters"
    }

    override val artifactType = COUNTERS

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.counterSessions

    override fun addSessionTab(pid: Int, session: CounterSession, sessionLifetime: Lifetime) {
        CounterSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: CounterSessionModel): CounterSession {
        val metrics = model.metrics.ifEmpty { null }
        return CounterSession(
            model.interval,
            model.providers,
            metrics,
            model.maxTimeSeries,
            model.maxHistograms,
        )
    }
}