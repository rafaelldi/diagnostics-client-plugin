package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.model.CounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterProtocolSessionTab
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
class CounterSessionTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): CounterSessionTabManager = project.service()
    }

    fun addSessionTab(sessionLifetime: Lifetime, pid: Int, session: CounterSession) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val contentFactory = ContentFactory.getInstance()
        val counterProtocolSessionTab = CounterProtocolSessionTab(pid, session, this, sessionLifetime)
        val content = contentFactory.createContent(counterProtocolSessionTab, "Counters for $pid", true)
        content.icon = AllIcons.Debugger.Db_primitive
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        sessionLifetime.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(sessionLifetime) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(AllIcons.Debugger.Db_primitive)
            val toolWindow =
                ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.Debugger.Db_primitive
        }
    }

    fun tabClosed(pid: Int) {
        CounterSessionController.getInstance(project).closeSession(pid)
    }

    fun activateTab(pid: Int) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val content = toolWindow.contentManager.findContent("Counters for $pid") ?: return
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }
}