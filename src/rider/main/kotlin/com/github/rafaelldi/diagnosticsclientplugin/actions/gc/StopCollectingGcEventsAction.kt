package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.GcEventCollectionSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class StopCollectingGcEventsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val pid = tab.selectedProcessId ?: return
        GcEventCollectionSessionController.getInstance(project).stopSession(pid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabledAndVisible = false
        } else {
            val selected = tab.selectedProcessId
            if (selected == null) {
                event.presentation.isEnabledAndVisible = false
            } else {
                val model = project.solution.diagnosticsHostModel
                event.presentation.isEnabledAndVisible = model.gcEventsCollectionSessions.contains(selected)
            }
        }
    }
}