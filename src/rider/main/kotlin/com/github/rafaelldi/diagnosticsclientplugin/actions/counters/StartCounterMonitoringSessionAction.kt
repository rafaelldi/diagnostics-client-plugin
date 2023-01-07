package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartMonitoringSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterLiveSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterMonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterMonitoringTab.Companion.COUNTER_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartCounterMonitoringSessionAction :
    StartMonitoringSessionAction<CounterMonitoringSession, CounterMonitoringTab>() {
    override val tabDatKey = COUNTER_MONITORING_TAB

    override fun startSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterLiveSessionController
                .getInstance(project)
                .startExistingSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.counterMonitoringSessions[pid]
}