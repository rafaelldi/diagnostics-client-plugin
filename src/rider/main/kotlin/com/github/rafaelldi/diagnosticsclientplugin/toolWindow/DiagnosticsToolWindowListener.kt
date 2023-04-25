package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsToolWindowFactory.Companion.DIAGNOSTICS_CLIENT_TOOL_WINDOW
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.jetbrains.rider.projectView.solution

class DiagnosticsToolWindowListener(private val project: Project) : ToolWindowManagerListener {
    override fun toolWindowShown(toolWindow: ToolWindow) {
        if (toolWindow.id != DIAGNOSTICS_CLIENT_TOOL_WINDOW) return

        val model = project.solution.diagnosticsHostModel
        if (model.processList.active.valueOrNull == true) return

        model.processList.active.set(true)
    }
}