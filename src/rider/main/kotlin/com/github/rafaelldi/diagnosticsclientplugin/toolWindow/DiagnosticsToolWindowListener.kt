package com.github.rafaelldi.diagnosticsclientplugin.toolWindow

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.jetbrains.rider.projectView.solution

class DiagnosticsToolWindowListener(private val project: Project) : ToolWindowManagerListener {
    companion object {
        private const val DIAGNOSTICS_CLIENT = "Diagnostics Client"
    }

    override fun stateChanged(
        toolWindowManager: ToolWindowManager,
        changeType: ToolWindowManagerListener.ToolWindowManagerEventType
    ) {
        val toolWindow = toolWindowManager.getToolWindow(DIAGNOSTICS_CLIENT) ?: return
        val model = project.solution.diagnosticsHostModel

        when (changeType) {
            ToolWindowManagerListener.ToolWindowManagerEventType.ActivateToolWindow -> {
                if (toolWindow.isAvailable && toolWindow.isVisible) {
                    model.processList.active.set(true)
                }
            }

            ToolWindowManagerListener.ToolWindowManagerEventType.HideToolWindow -> {
                if (toolWindow.isAvailable && toolWindow.isVisible.not()) {
                    model.processList.active.set(false)
                }
            }

            else -> {
                //ignore
            }
        }
    }
}