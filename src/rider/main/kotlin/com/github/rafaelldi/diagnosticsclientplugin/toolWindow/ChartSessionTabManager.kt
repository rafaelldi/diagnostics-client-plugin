package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.model.LiveChartSession
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.LiveChartSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveChartSessionTab
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

@Service
class ChartSessionTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): ChartSessionTabManager = project.service()
    }

    fun addSessionTab(sessionLifetime: Lifetime, pid: Int, session: LiveChartSession) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
        val contentFactory = ContentFactory.getInstance()
        val liveChartSessionTab = LiveChartSessionTab(pid, session, this, sessionLifetime)
        val content = contentFactory.createContent(liveChartSessionTab, "Chart for $pid", true)
        content.icon = AllIcons.RunConfigurations.TestCustom
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        sessionLifetime.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
        session.active.advise(sessionLifetime) { sessionStatusChanged(it, content) }
    }

    private fun sessionStatusChanged(isActive: Boolean, content: Content) {
        if (isActive) {
            content.icon = ExecutionUtil.getLiveIndicator(AllIcons.RunConfigurations.TestCustom)
            val toolWindow =
                ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW) ?: return
            toolWindow.contentManager.setSelectedContent(content, true, true)
        } else {
            content.icon = AllIcons.RunConfigurations.TestCustom
        }
    }

    fun tabClosed(pid: Int) {
        LiveChartSessionController.getInstance(project).closeSession(pid)
    }
}