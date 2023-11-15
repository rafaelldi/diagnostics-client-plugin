package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.model.TraceProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.TraceProtocolSessionTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.util.lifetime.Lifetime

@Service(Service.Level.PROJECT)
class TraceSessionTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): TraceSessionTabManager = project.service()
    }

    fun addSessionTab(sessionLifetime: Lifetime, pid: Int, session: TraceProtocolSession) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val contentFactory = ContentFactory.getInstance()
        val traceProtocolSessionTab = TraceProtocolSessionTab(pid, session, this, project, sessionLifetime)
        val content = contentFactory.createContent(traceProtocolSessionTab, "Traces for $pid", true)
        content.icon = AllIcons.Toolwindows.ToolWindowMessages
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        sessionLifetime.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(sessionLifetime) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(AllIcons.Toolwindows.ToolWindowMessages)
            val toolWindow =
                ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.Toolwindows.ToolWindowMessages
        }
    }

    fun tabClosed(pid: Int) {
        TraceSessionController.getInstance(project).closeSession(pid)
    }

    fun activateTab(pid: Int) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val content = toolWindow.contentManager.findContent("Traces for $pid") ?: return
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }
}