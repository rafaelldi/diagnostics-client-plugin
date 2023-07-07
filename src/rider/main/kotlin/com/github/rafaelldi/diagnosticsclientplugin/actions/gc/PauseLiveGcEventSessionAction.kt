package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.LiveGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveGcEventSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveGcEventSessionTab.Companion.GC_EVENT_MONITORING_TAB
import com.intellij.openapi.project.Project

class PauseLiveGcEventSessionAction : PauseLiveSessionAction<LiveGcEventSession, LiveGcEventSessionTab>() {
    override val tabDatKey = GC_EVENT_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        LiveGcEventSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        LiveGcEventSessionController.getInstance(project).getSession(pid)
}