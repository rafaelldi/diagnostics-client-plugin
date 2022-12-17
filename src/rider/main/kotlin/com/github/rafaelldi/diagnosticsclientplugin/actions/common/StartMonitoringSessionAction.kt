package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.generated.MonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitoringTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.project.Project

abstract class StartMonitoringSessionAction<TSession : MonitoringSession, TTab : MonitoringTab> : AnAction() {
    protected abstract val tabDatKey: DataKey<TTab>

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(tabDatKey) ?: return
        startSession(tab.pid, project)
    }

    protected abstract fun startSession(pid: Int, project: Project)

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(tabDatKey)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val pid = tab.pid
            val session = getSession(pid, project)
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = !isActive
        }
    }

    protected abstract fun getSession(pid: Int, project: Project): TSession?

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}