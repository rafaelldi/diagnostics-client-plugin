package com.github.rafaelldi.diagnosticsclientplugin.actions.quick

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.LiveTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StartLiveTraceSessionQuickAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val selected = tab.selectedProcess ?: return
        val session = project.solution.diagnosticsHostModel.liveTraceSessions[selected.pid]
        if (session == null) {
            val dialog = TraceDialog(project, selected, tab.processes, false)
            if (dialog.showAndGet()) {
                val model = dialog.getModel()
                TraceSettings.getInstance(project).update(model, false)
                LiveTraceSessionController.getInstance(project).startSession(model)
            }
        } else {
            TraceTabManager.getInstance(project).activateTab(selected.pid)
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val selected = tab.selectedProcess
            event.presentation.isEnabled = selected != null
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}