package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.model.Session
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.toDotNetProcess
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class StartSessionAction<TSession : Session> : AnAction() {
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

        val session = getSession(processNode.processId, project)
        event.presentation.isEnabledAndVisible = session == null
    }

    protected abstract fun getSession(pid: Int, project: Project): TSession?

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}