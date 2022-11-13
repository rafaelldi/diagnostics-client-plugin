package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorGcTab.Companion.MONITOR_GC_TAB
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StartGcEventMonitoringSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(MONITOR_GC_TAB) ?: return
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            val pid = tab.pid
            GcEventMonitoringSessionController
                .getInstance(project)
                .startExistingSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun update(event: AnActionEvent) {
        val tab = event.getData(MONITOR_GC_TAB)
        val project = event.project
        if (tab == null || project == null) {
            event.presentation.isEnabled = false
        } else {
            val pid = tab.pid
            val model = project.solution.diagnosticsHostModel
            val session = model.gcEventMonitoringSessions[pid]
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = !isActive
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}