package com.github.rafaelldi.diagnosticsclientplugin.actions.chart

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveChartSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSettings
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.LiveChartSessionController
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StartLiveChartSessionAction : StartLiveSessionAction<LiveChartSession>() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = ChartDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            ChartSettings.getInstance().update(model)
            LiveChartSessionController.getInstance(project).startSession(model)
        }
    }

    override fun getSession(selected: DotNetProcess, project: Project) =
        project.solution.diagnosticsHostModel.liveChartSessions[selected.pid]
}