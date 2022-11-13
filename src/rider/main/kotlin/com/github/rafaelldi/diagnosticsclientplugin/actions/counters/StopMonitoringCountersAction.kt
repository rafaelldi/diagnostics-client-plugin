package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab.Companion.PROCESS_EXPLORE_TAB
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StopMonitoringCountersAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(PROCESS_EXPLORE_TAB) ?: return
        val pid = tab.selectedProcessId ?: return
        CounterMonitoringSessionController.getInstance(project).stopSession(pid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabledAndVisible = false
        } else {
            val selected = tab.selectedProcessId
            if (selected == null) {
                event.presentation.isEnabledAndVisible = false
            } else {
                val model = project.solution.diagnosticsHostModel
                val session = model.counterMonitoringSessions[selected]
                val isActive = session?.active?.valueOrNull ?: false
                event.presentation.isEnabledAndVisible = isActive
            }
        }
    }
}