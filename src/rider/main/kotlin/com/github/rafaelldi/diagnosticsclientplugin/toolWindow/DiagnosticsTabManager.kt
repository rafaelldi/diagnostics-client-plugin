package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.projectView.solution

@Service
class DiagnosticsTabManager(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        private const val DIAGNOSTICS_CLIENT_TOOL_WINDOW = "Diagnostics Client"
        fun getInstance(project: Project): DiagnosticsTabManager = project.service()
        fun getToolWindow(project: Project): ToolWindow? =
            ToolWindowManager.getInstance(project).getToolWindow(DIAGNOSTICS_CLIENT_TOOL_WINDOW)
    }

    private val counterTabManager: CounterTabManager = project.service()
    private val gcEventTabManager: GcEventTabManager = project.service()
    private val traceTabManager: TraceTabManager = project.service()

    fun createExplorerTab(toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val processExplorerTab =
            ProcessExplorerTab(project.solution.diagnosticsHostModel.processList, projectComponentLifetime)
        val content = contentFactory.createContent(processExplorerTab, "Explorer", true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)
    }
}