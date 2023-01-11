package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.LiveGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveGcEventSessionTab
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.LiveGcEventSessionTab.Companion.GC_EVENT_MONITORING_TAB
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

class ResumeLiveGcEventSessionAction : ResumeLiveSessionAction<LiveGcEventSession, LiveGcEventSessionTab>() {
    override val tabDatKey = GC_EVENT_MONITORING_TAB

    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            LiveGcEventSessionController
                .getInstance(project)
                .startExistingSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        project.solution.diagnosticsHostModel.liveGcEventSessions[pid]
}