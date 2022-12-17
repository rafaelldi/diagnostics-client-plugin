package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartMonitoringAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorCountersDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterMonitoringSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSettings
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartMonitoringCountersAction : StartMonitoringAction<CounterMonitoringSession>() {
    override fun startSession(pid: Int, project: Project) {
        val dialog = MonitorCountersDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CounterSettings.getInstance(project).update(model)
            CounterMonitoringSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.counterMonitoringSessions[pid]
}