package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionListener
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceMonitoringTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution
import icons.DiagnosticsClientIcons

@Service
class TraceTabManager(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): TraceTabManager = project.service()
    }

    init {
        val model = project.solution.diagnosticsHostModel
        model.liveTraceSessions.view(projectComponentLifetime) { lt, pid, session ->
            addTraceSessionTab(lt, pid, session)
        }
    }

    private fun addTraceSessionTab(lt: Lifetime, pid: Int, session: LiveTraceSession) {
        val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
        val contentFactory = ContentFactory.getInstance()
        val traceMonitoringTab = TraceMonitoringTab(pid, session, this, project, lt)
        val content = contentFactory.createContent(traceMonitoringTab, "Traces for $pid", true)
        content.icon = DiagnosticsClientIcons.Traces
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        lt.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(lt) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(DiagnosticsClientIcons.Traces)
            val toolWindow = DiagnosticsTabManager.getToolWindow(project) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = DiagnosticsClientIcons.Traces
        }
    }

    fun tabClosed(pid: Int) {
        project.messageBus.syncPublisher(TraceSessionListener.TOPIC).sessionClosed(pid)
    }
}