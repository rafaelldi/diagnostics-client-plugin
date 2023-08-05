package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterProtocolSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.CounterProtocolSessionTab.Companion.COUNTER_MONITORING_TAB
import com.intellij.openapi.project.Project

class PauseLiveCounterSessionAction : PauseLiveSessionAction<CounterProtocolSession, CounterProtocolSessionTab>() {
    override val tabDatKey = COUNTER_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        CounterProtocolSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        CounterProtocolSessionController.getInstance(project).getSession(pid)
}