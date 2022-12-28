package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class StartCollectingAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val selected = tab.selectedProcess ?: return
        startSession(selected, tab.processes, project)
    }

    protected abstract fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project)

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val selected = tab.selectedProcess
            if (selected == null) {
                event.presentation.isEnabled = false
            } else {
                event.presentation.isEnabledAndVisible = !containsSession(selected, project)
            }
        }
    }

    protected abstract fun containsSession(selected: DotNetProcess, project: Project): Boolean

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}