package com.github.rafaelldi.diagnosticsclientplugin.actions.common

import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.MonitoringTab
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.project.Project

abstract class ResumeLiveSessionAction<TSession : LiveSession, TTab : MonitoringTab> : AnAction() {
    protected abstract val tabDatKey: DataKey<TTab>

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val tab = event.getData(tabDatKey) ?: return
        resumeSession(tab.pid, project)
    }

    protected abstract fun resumeSession(pid: Int, project: Project)

    override fun update(event: AnActionEvent) {
        val project = event.project
        val tab = event.getData(tabDatKey)
        if (project == null || tab == null) {
            event.presentation.isEnabled = false
        } else {
            val session = getSession(tab.pid, project)
            val isActive = session?.active?.valueOrNull ?: false
            event.presentation.isEnabled = !isActive
        }
    }

    protected abstract fun getSession(pid: Int, project: Project): TSession?

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
}