package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CountersSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab.Companion.PROCESS_EXPLORE_TAB
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StartCollectingCountersAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(PROCESS_EXPLORE_TAB) ?: return
        val pid = tab.selectedProcessId ?: return
        val dialog = CollectCountersDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            CountersSettings.getInstance(project).update(model)
            CounterCollectionSessionController.getInstance(project).startSession(pid, model)
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val selected = tab.selectedProcessId
            if (selected == null) {
                event.presentation.isEnabled = false
            } else {
                val model = project.solution.diagnosticsHostModel
                event.presentation.isEnabledAndVisible = !model.counterCollectionSessions.contains(selected)
            }
        }
    }
}