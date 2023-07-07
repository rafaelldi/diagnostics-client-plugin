package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalRootNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class DisconnectFromAgentAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE) ?: return

        if (tree.selectedNode !is LocalRootNode) return

        val host = DiagnosticsHost.getInstance(project)
        if (!host.isConnected) return

        host.disconnectFromAgent()
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (project == null || tree == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val localRoot = tree.selectedNode as? LocalRootNode
        if (localRoot == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val host = DiagnosticsHost.getInstance(project)
        event.presentation.isEnabledAndVisible = host.isConnected
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}