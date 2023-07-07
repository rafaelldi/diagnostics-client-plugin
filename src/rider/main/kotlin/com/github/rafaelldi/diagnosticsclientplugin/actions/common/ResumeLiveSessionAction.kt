package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.model.LiveSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.LocalProcessNode
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.ProcessExplorerTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.project.Project

abstract class ResumeLiveSessionAction<TSession : LiveSession, TTab : MonitoringTab> : AnAction() {
    protected abstract val tabDatKey: DataKey<TTab>

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val pid = getProcessId(event) ?: return
        resumeSession(pid, project)
    }

    protected abstract fun resumeSession(pid: Int, project: Project)

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
            event.presentation.isEnabledAndVisible = !isActive
        }
    }

    private fun getProcessId(event: AnActionEvent): Int? {
        val tab = event.getData(tabDatKey)
        if (tab != null) return tab.pid

        val tree = event.getData(ProcessExplorerTab.PROCESS_TREE)
        if (tree != null) return (tree.selectedNode as? LocalProcessNode)?.processId

        return null
    }

    protected abstract fun getSession(pid: Int, project: Project): TSession?

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}