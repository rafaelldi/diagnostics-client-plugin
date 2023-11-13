package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterProtocolSessionTab
import com.intellij.openapi.project.Project

class PauseCounterSessionAction : PauseSessionAction<CounterSession, CounterProtocolSessionTab>() {
    override fun pauseSession(pid: Int, project: Project) {
        CounterSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        CounterSessionController.getInstance(project).getSession(pid)
}