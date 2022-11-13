package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.GcEventMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorGcTab.Companion.MONITOR_GC_TAB
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StopGcEventMonitoringSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(MONITOR_GC_TAB) ?: return
        val pid = tab.getSessionPid()
        GcEventMonitoringSessionController.getInstance(project).stopExistingSession(pid)
    }

    override fun update(event: AnActionEvent) {
        val tab = event.getData(MONITOR_GC_TAB)
        val project = event.project
        if (tab == null || project == null) {
            event.presentation.isEnabled = false
        } else {
            val pid = tab.getSessionPid()
            val model = project.solution.diagnosticsHostModel
            val session = model.gcEventsMonitoringSessions[pid]
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = isActive
        }
    }
}