package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.model.Session
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class PauseSessionAction<TSession : Session, TTab : MonitoringTab> : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = getProcessId(event) ?: return
        pauseSession(pid, project)
    }

    protected abstract fun pauseSession(pid: Int, project: Project)

    override fun update(event: AnActionEvent) {
        val project = event.project
        val pid = getProcessId(event)
        if (project == null || pid == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val session = getSession(pid, project)
        val isActive = session?.active?.valueOrNull
        if (isActive == null) {
            event.presentation.isEnabledAndVisible = false
        } else {
            event.presentation.isEnabledAndVisible = isActive
        }
    }

    private fun getProcessId(event: AnActionEvent): Int? {
        val sessionPid = event.getData(SESSION_PROCESS_ID)
        if (sessionPid != null) return sessionPid

        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (tree != null) return (tree.selectedNode as? LocalProcessNode)?.processId

        return null
    }

    protected abstract fun getSession(pid: Int, project: Project): TSession?

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}