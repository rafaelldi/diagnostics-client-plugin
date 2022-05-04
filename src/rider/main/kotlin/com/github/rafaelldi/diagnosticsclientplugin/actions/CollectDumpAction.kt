package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectDumpDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.DumpCollectionController
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.jetbrains.rider.projectView.solution

class CollectDumpAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = project.solution.diagnosticsHostModel.processList.selected.value ?: return
        val dialog = CollectDumpDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            val controller = project.service<DumpCollectionController>()
            controller.collectDump(pid, model)
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            event.presentation.isEnabled = false
        } else {
            event.presentation.isEnabled = project.solution.diagnosticsHostModel.processList.selected.value != null
        }
    }
}