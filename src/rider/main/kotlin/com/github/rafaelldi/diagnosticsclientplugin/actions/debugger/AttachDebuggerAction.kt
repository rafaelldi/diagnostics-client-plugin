package com.github.rafaelldi.diagnosticsclientplugin.actions.debugger

import com.github.rafaelldi.diagnosticsclientplugin.services.debugger.AttachDebuggerService
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.rd.util.launchBackground
import com.intellij.openapi.rd.util.lifetime

class AttachDebuggerAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = getProcessId(event) ?: return
        project.lifetime.launchBackground {
            AttachDebuggerService.getInstance(project).attach(pid)
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
        val sessionPid = event.getData(SESSION_PROCESS_ID)
        if (sessionPid != null) return sessionPid

        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (tree != null) return (tree.selectedNode as? LocalProcessNode)?.processId

        return null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}