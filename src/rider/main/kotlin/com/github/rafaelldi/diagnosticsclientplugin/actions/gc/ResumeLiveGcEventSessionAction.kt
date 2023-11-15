package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventProtocolSessionTab
import com.intellij.openapi.project.Project

class ResumeLiveGcEventSessionAction : ResumeLiveSessionAction<GcEventProtocolSession, GcEventProtocolSessionTab>() {
    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventProtocolSessionController
                .getInstance(project)
                .resumeSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        GcEventProtocolSessionController.getInstance(project).getSession(pid)
}