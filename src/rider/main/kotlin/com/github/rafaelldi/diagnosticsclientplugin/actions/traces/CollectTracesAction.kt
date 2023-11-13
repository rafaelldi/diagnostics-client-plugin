package com.github.rafaelldi.diagnosticsclientplugin.actions.traces

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.TraceSessionDialog
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.toDotNetProcess
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.openapi.rd.util.lifetime

class CollectTracesAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE) ?: return

        val selectedProcessNode = tree.selectedNode as? LocalProcessNode ?: return
        val selectedProcess = selectedProcessNode.toDotNetProcess()
        val processes = tree.getLocalProcessNodes().map { it.toDotNetProcess() }

        val dialog = TraceSessionDialog(project, selectedProcess, processes, true)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            TraceSettings.getInstance(project).update(model, true)
            project.lifetime.launchOnUi {
                TraceController.getInstance(project).collect(model)
            }
        }
    }

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

        event.presentation.isEnabledAndVisible = true
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}