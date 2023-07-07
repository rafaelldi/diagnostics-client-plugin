package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.RecentArtifactTab
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class DiagnosticsToolWindowFactory : ToolWindowFactory, DumbAware {
    companion object {
        const val DIAGNOSTICS_CLIENT_TOOL_WINDOW = "Diagnostics"
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        createExplorerTab(project, toolWindow)
        createRecentArtifactTab(project, toolWindow)
    }

    private fun createExplorerTab(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val processExplorerTab = ProcessExplorerTab(project)
        val content = contentFactory.createContent(processExplorerTab, DiagnosticsClientBundle.message("tool.window.explorer"), true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)
    }

    private fun createRecentArtifactTab(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val recentArtifactTab = RecentArtifactTab(project)
        val content = contentFactory.createContent(recentArtifactTab, DiagnosticsClientBundle.message("tool.window.recent.artifacts"), true)
        content.isCloseable = false
        toolWindow.contentManager.addContent(content)
    }
}