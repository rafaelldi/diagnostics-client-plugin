package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.toDotNetProcess
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class StartExportSessionAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE) ?: return

        val selectedProcessNode = tree.selectedNode as? LocalProcessNode ?: return
        val selectedProcess = selectedProcessNode.toDotNetProcess()
        val processes = tree.getLocalProcessNodes().map { it.toDotNetProcess() }

        startSession(selectedProcess, processes, project)
    }

    protected abstract fun startSession(selected: DotNetProcess, processes: List<DotNetProcess>, project: Project)

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

        event.presentation.isEnabledAndVisible = containsSession(processNode.processId, project) == false
    }

    protected abstract fun containsSession(pid: Int, project: Project): Boolean?

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}