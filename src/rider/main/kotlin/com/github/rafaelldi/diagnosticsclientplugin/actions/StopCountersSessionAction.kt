package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.CountersMonitoringSessionsController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsClientDataKeys
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.jetbrains.rider.projectView.solution

class StopCountersSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(DiagnosticsClientDataKeys.MONITOR_COUNTERS_TAB) ?: return
        val pid = tab.getSessionPid()
        val controller = project.service<CountersMonitoringSessionsController>()
        controller.stopExistingSession(pid)
    }

    override fun update(event: AnActionEvent) {
        val tab = event.getData(DiagnosticsClientDataKeys.MONITOR_COUNTERS_TAB)
        val project = event.project
        if (tab == null || project == null) {
            event.presentation.isEnabled = false
        } else {
            val pid = tab.getSessionPid()
            val model = project.solution.diagnosticsHostModel
            val session = model.countersMonitoringSessions[pid]
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = isActive
        }
    }
}