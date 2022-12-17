package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterMonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterMonitoringTab.Companion.COUNTER_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopCounterMonitoringSessionAction :
    StopMonitoringSessionAction<CounterMonitoringSession, CounterMonitoringTab>() {
    override val tabDatKey = COUNTER_MONITORING_TAB

    override fun stopSession(pid: Int, project: Project) {
        CounterMonitoringSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.counterMonitoringSessions[pid]
}