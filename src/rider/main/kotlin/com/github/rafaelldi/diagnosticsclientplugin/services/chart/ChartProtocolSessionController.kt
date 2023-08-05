package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.ChartSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service
class ChartProtocolSessionController(project: Project) :
    ProtocolSessionController<ChartProtocolSession, ChartSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): ChartProtocolSessionController = project.service()
        private const val CHART = "Chart"
    }

    override val artifactType = CHART

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.chartProtocolSessions

    override fun addSessionTab(pid: Int, session: ChartProtocolSession, sessionLifetime: Lifetime) {
        ChartSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: ChartSessionModel) = ChartProtocolSession()
}