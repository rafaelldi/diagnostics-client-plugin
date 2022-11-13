package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectDumpDialog
import com.github.rafaelldi.diagnosticsclientplugin.services.DumpCollectionController
import com.github.rafaelldi.diagnosticsclientplugin.services.DumpSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchOnUi
import com.jetbrains.rd.platform.util.lifetime

class CollectDumpAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB) ?: return
        val pid = tab.selectedProcessId ?: return
        val dialog = CollectDumpDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            DumpSettings.getInstance(project).update(model)
            project.lifetime.launchOnUi {
                DumpCollectionController.getInstance(project).collectDump(pid, model)
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(ProcessExplorerTab.PROCESS_EXPLORE_TAB)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            event.presentation.isEnabled = tab.selectedProcessId != null
        }
    }
}