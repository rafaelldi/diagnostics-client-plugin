package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.RecentArtifactTab
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.ContentFactory
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rider.projectView.solution

@Service
class DiagnosticsTabManager(private val project: Project) {
    companion object {
        fun getInstance(project: Project): DiagnosticsTabManager = project.service()
    }

    fun createExplorerTab(toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val processExplorerTab =
            ProcessExplorerTab(project.solution.diagnosticsHostModel.processList, project.lifetime)
        val content = contentFactory.createContent(processExplorerTab, DiagnosticsClientBundle.message("tool.window.explorer"), true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)

        val model = project.solution.diagnosticsHostModel
        if (toolWindow.isVisible) {
            model.processList.active.set(true)
        }
    }

    fun createRecentArtifactTab(toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val recentArtifactTab = RecentArtifactTab(project, project.lifetime)
        val content = contentFactory.createContent(recentArtifactTab, DiagnosticsClientBundle.message("tool.window.recent.artifacts"), true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)
    }
}