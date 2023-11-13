package com.github.rafaelldi.diagnosticsclientplugin.actions.quick

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartSessionQuickAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartSession
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.ChartSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project

class StartChartSessionQuickAction : StartSessionQuickAction<ChartSession>() {
    override fun getSession(pid: Int, project: Project) =
        ChartSessionController.getInstance(project).getSession(pid)

    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = ChartSessionDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            ChartSettings.getInstance().update(model)
            ChartSessionController.getInstance(project).startSession(model)
        }
    }

    override fun activateTab(pid: Int, project: Project) {
        ChartSessionTabManager.getInstance(project).activateTab(pid)
    }
}