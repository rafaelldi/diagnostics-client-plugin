package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.CountersMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorCountersTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.put
import com.jetbrains.rider.projectView.solution
import icons.DiagnosticsClientIcons

@Service
class DiagnosticsTabsManager(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        private const val DIAGNOSTICS_CLIENT_TOOL_WINDOW = "Diagnostics Client"
        fun getToolWindow(project: Project): ToolWindow? =
            ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW)
    }

    private val countersTabContents: MutableMap<Int, Content> = mutableMapOf()

    fun createExplorerTab(toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val processExplorerTab =
            ProcessExplorerTab(project.solution.diagnosticsHostModel.processList, projectComponentLifetime)
        val content = contentFactory.createContent(processExplorerTab, "Explorer", true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)
    }

    fun createCountersTab(lt: Lifetime, session: CountersMonitoringSession) {
        val toolWindow = getToolWindow(project) ?: return
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val monitorCountersTab = MonitorCountersTab(session, this, lt)
        val content = contentFactory.createContent(monitorCountersTab, "Counters for ${session.pid}", true)
        content.icon = DiagnosticsClientIcons.Counters
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        countersTabContents.put(lt, session.pid, content)
        lt.bracket(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
    }

    fun activateCountersSessionTab(pid: Int) {
        val toolWindow = getToolWindow(project) ?: return
        val content = countersTabContents[pid] ?: return
        content.icon = ExecutionUtil.getLiveIndicator(DiagnosticsClientIcons.Counters)
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }

    fun deactivateCountersSessionTab(pid: Int) {
        val content = countersTabContents[pid] ?: return
        content.icon = DiagnosticsClientIcons.Counters
    }
}