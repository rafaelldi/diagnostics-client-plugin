package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.CountersMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventsMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorCountersTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorGcTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.put
import com.jetbrains.rider.projectView.solution

@Service
class DiagnosticsTabsManager(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        private const val DIAGNOSTICS_CLIENT_TOOL_WINDOW = "Diagnostics Client"

        fun getInstance(project: Project): DiagnosticsTabsManager = project.service()

        fun getToolWindow(project: Project): ToolWindow? =
            ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW)
    }

    private val counterTabsManager: CounterTabsManager = project.service()
    private val gcTabContents: MutableMap<Int, Content> = mutableMapOf()

    fun createExplorerTab(toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val processExplorerTab =
            ProcessExplorerTab(project.solution.diagnosticsHostModel.processList, projectComponentLifetime)
        val content = contentFactory.createContent(processExplorerTab, "Explorer", true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)
    }

    fun createGcTab(lt: Lifetime, session: GcEventsMonitoringSession) {
        val toolWindow = getToolWindow(project) ?: return
        val contentFactory = ContentFactory.getInstance()
        val monitorGcTab = MonitorGcTab(session, this, lt)
        val content = contentFactory.createContent(monitorGcTab, "GC for ${session.pid}", true)
        content.icon = AllIcons.Actions.GC
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, true)
        gcTabContents.put(lt, session.pid, content)
        lt.bracketIfAlive(
            { toolWindow.contentManager.addContent(content) },
            { toolWindow.contentManager.removeContent(content, true) }
        )
    }

    fun activateGcSessionTab(pid: Int) {
        val toolWindow = getToolWindow(project) ?: return
        val content = gcTabContents[pid] ?: return
        content.icon = ExecutionUtil.getLiveIndicator(AllIcons.Actions.GC)
        toolWindow.contentManager.setSelectedContent(content, true, true)
    }

    fun deactivateGcSessionTab(pid: Int) {
        val content = gcTabContents[pid] ?: return
        content.icon = AllIcons.Actions.GC
    }
}