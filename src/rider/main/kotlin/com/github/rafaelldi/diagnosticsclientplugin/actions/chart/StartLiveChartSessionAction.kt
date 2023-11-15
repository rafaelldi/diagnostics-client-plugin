package com.github.rafaelldi.diagnosticsclientplugin.actions.chart

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StartLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ChartSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSettings
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class StartLiveChartSessionAction : StartLiveSessionAction<ChartProtocolSession>() {
    override fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project) {
        val dialog = ChartSessionDialog(project, selected, processes)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            ChartSettings.getInstance().update(model)
            ChartProtocolSessionController.getInstance(project).startSession(model)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        ChartProtocolSessionController.getInstance(project).getSession(pid)

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (project == null || tree == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val processNode = tree.selectedNode as? LocalProcessNode
        if (processNode == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val session = getSession(processNode.processId, project)
        event.presentation.isVisible = true
        event.presentation.isEnabled = session == null
    }
}