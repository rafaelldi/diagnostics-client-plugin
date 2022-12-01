package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorTracesDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StartMonitoringTracesAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val pid = tab.selectedProcessId ?: return
        val dialog = MonitorTracesDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model)
            TraceMonitoringSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val selected = tab.selectedProcessId
            if (selected == null) {
                event.presentation.isEnabled = false
            } else {
                val model = project.solution.diagnosticsHostModel
                val session = model.traceMonitoringSessions[selected]
                val isActive = session?.active?.valueOrNull ?: false
                event.presentation.isEnabledAndVisible = !isActive
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}