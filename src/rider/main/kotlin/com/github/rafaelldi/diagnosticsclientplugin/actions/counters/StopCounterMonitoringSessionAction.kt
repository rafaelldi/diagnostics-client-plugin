package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitorCountersTab.Companion.MONITOR_COUNTERS_TAB
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StopCounterMonitoringSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(MONITOR_COUNTERS_TAB) ?: return
        val pid = tab.pid
        CounterMonitoringSessionController.getInstance(project).stopSession(pid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(MONITOR_COUNTERS_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val pid = tab.pid
            val model = project.solution.diagnosticsHostModel
            val session = model.counterMonitoringSessions[pid]
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = isActive
        }
    }
}