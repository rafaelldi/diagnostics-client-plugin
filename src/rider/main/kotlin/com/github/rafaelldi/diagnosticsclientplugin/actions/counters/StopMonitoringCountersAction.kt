package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterLiveSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopMonitoringCountersAction : StopMonitoringAction<CounterMonitoringSession>() {
    override fun stopSession(selected: DotNetProcess, project: Project) {
        CounterLiveSessionController.getInstance(project).stopSession(selected.pid)
    }

    override fun getSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.counterMonitoringSessions[selected.pid]
}