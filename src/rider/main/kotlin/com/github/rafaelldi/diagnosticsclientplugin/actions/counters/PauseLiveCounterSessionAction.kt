package com.github.rafaelldi.diagnosticsclientplugin.actions.counters

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.LiveCounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveCounterSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveCounterSessionTab.Companion.COUNTER_MONITORING_TAB
import com.intellij.openapi.project.Project

class PauseLiveCounterSessionAction : PauseLiveSessionAction<LiveCounterSession, LiveCounterSessionTab>() {
    override val tabDatKey = COUNTER_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        LiveCounterSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        LiveCounterSessionController.getInstance(project).getSession(pid)
}