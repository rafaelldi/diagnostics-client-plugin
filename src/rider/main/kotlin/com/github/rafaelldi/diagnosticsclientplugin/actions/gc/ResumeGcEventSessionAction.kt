package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.ResumeSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringTimerDialog
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventProtocolSessionTab
import com.intellij.openapi.project.Project

class ResumeGcEventSessionAction : ResumeSessionAction<GcEventSession, GcEventProtocolSessionTab>() {
    override fun resumeSession(pid: Int, project: Project) {
        val dialog = MonitoringTimerDialog(project)
        if (dialog.showAndGet()) {
            val model = dialog.getModel()
            GcEventSessionController
                .getInstance(project)
                .resumeSession(pid, model.stoppingType, model.duration)
        }
    }

    override fun getSession(pid: Int, project: Project) =
        GcEventSessionController.getInstance(project).getSession(pid)
}