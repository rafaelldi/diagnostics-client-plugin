package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.TriggerGcCollectionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventMonitoringTab.Companion.GC_EVENT_MONITORING_TAB
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.rider.projectView.solution

class TriggerGcCollectionSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(GC_EVENT_MONITORING_TAB) ?: return
        TriggerGcCollectionController.getInstance(project).triggerGc(tab.pid)
    }

    override fun update(event: AnActionEvent) {
        val tab = event.getData(GC_EVENT_MONITORING_TAB)
        val project = event.project
        if (tab == null || project == null) {
            event.presentation.isEnabled = false
        } else {
            val model = project.solution.diagnosticsHostModel
            val session = model.liveGcEventSessions[tab.pid]
            event.presentation.isEnabled = session != null
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}