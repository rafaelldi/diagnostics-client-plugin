package com.github.rafaelldi.diagnosticsclientplugin.actions.quick

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartLiveSessionQuickAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.ChartSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartLiveChartSessionQuickAction : StartLiveSessionQuickAction<ChartProtocolSession>() {
    override fun getSession(pid: Int, project: Project) =
        ChartProtocolSessionController.getInstance(project).getSession(pid)

    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = ChartSessionDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            ChartSettings.getInstance().update(model)
            ChartProtocolSessionController.getInstance(project).startSession(model)
        }
    }

    override fun activateTab(pid: Int, project: Project) {
        ChartSessionTabManager.getInstance(project).activateTab(pid)
    }
}