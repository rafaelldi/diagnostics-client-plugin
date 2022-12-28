package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.services.gc.TriggerGcCollectionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class TriggerGcCollectionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val process = tab.selectedProcess ?: return
        TriggerGcCollectionController.getInstance(project).triggerGc(process.pid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            event.presentation.isEnabled = tab.selectedProcess != null
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}