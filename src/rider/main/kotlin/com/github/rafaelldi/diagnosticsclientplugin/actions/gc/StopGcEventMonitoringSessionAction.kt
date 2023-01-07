package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopMonitoringSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventLiveSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventMonitoringTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventMonitoringTab.Companion.GC_EVENT_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class StopGcEventMonitoringSessionAction :
    StopMonitoringSessionAction<GcEventMonitoringSession, GcEventMonitoringTab>() {
    override val tabDatKey = GC_EVENT_MONITORING_TAB

    override fun stopSession(pid: Int, project: Project) {
        GcEventLiveSessionController.getInstance(project).stopSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.gcEventMonitoringSessions[pid]
}