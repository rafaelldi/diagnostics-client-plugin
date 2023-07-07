package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.services.gc.LiveGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.TriggerGcCollectionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveGcEventSessionTab.Companion.GC_EVENT_MONITORING_TAB
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class TriggerGcCollectionSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(GC_EVENT_MONITORING_TAB) ?: return
        TriggerGcCollectionController.getInstance(project).triggerGc(tab.pid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(GC_EVENT_MONITORING_TAB)
        if (tab == null || project == null) {
            event.presentation.isEnabled = false
        } else {
            val session = LiveGcEventSessionController.getInstance(project).getSession(tab.pid)
            event.presentation.isEnabled = session != null
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}