package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterMonitoringSessionController
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopMonitoringCountersAction : StopMonitoringAction<CounterMonitoringSession>() {
    override fun stopSession(pid: Int, project: Project) {
        CounterMonitoringSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.counterMonitoringSessions[pid]
}