package com.github.rafaelldi.diagnosticsclientplugin.actions.memoryDump

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MemoryDumpDialog
import com.github.rafaelldi.diagnosticsclientplugin.services.memoryDump.MemoryDumpController
import com.github.rafaelldi.diagnosticsclientplugin.services.memoryDump.MemoryDumpSettings
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ChartProtocolSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.openapi.rd.util.lifetime

class CollectMemoryDumpAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = getProcessId(event) ?: return
        val dialog = MemoryDumpDialog(pid, project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            MemoryDumpSettings.getInstance(project).update(model)
            project.lifetime.launchOnUi {
                MemoryDumpController.getInstance(project).collect(model)
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val pid = getProcessId(event)
        if (project == null || pid == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        event.presentation.isEnabledAndVisible = true
    }

    private fun getProcessId(event: AnActionEvent): Int? {
        val sessionPid = event.getData(ChartProtocolSessionTab.SESSION_PROCESS_ID)
        if (sessionPid != null) return sessionPid

        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (tree != null) return (tree.selectedNode as? LocalProcessNode)?.processId

        return null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}