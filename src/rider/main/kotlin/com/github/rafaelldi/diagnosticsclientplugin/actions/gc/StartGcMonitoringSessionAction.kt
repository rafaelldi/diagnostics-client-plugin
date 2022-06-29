package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.GcMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsClientDataKeys
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.jetbrains.rider.projectView.solution

class StartGcMonitoringSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(DiagnosticsClientDataKeys.MONITOR_GC_TAB) ?: return
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            val pid = tab.getSessionPid()
            val controller = project.service<GcMonitoringSessionController>()
            controller.startExistingSession(pid, model)
        }
    }

    override fun update(event: AnActionEvent) {
        val tab = event.getData(DiagnosticsClientDataKeys.MONITOR_GC_TAB)
        val project = event.project
        if (tab == null || project == null) {
            event.presentation.isEnabled = false
        } else {
            val pid = tab.getSessionPid()
            val model = project.solution.diagnosticsHostModel
            val session = model.gcMonitoringSessions[pid]
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = !isActive
        }
    }
}