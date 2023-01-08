package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.LiveGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventMonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventMonitoringTab.Companion.GC_EVENT_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class PauseLiveGcEventSessionAction : PauseLiveSessionAction<LiveGcEventSession, GcEventMonitoringTab>() {
    override val tabDatKey = GC_EVENT_MONITORING_TAB

    override fun pauseSession(pid: Int, project: Project) {
        LiveGcEventSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.liveGcEventSessions[pid]
}