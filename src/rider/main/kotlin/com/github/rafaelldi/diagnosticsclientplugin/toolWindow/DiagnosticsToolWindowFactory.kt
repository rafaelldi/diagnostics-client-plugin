package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class DiagnosticsToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        DiagnosticsTabManager.getInstance(project).createExplorerTab(toolWindow)
    }
}