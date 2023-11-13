package com.github.rafaelldi.diagnosticsclientplugin.services.chart

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.SessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.ChartSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service(Service.Level.PROJECT)
class ChartSessionController(project: Project) : SessionController<ChartSession, ChartSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): ChartSessionController = project.service()
        private const val CHART = "Chart"
    }

    override val artifactType = CHART

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.chartSessions

    override fun addSessionTab(pid: Int, session: ChartSession, sessionLifetime: Lifetime) {
        ChartSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: ChartSessionModel) = ChartSession()
}