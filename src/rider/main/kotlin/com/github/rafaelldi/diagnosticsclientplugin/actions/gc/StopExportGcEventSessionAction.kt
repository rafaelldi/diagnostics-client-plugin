package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.StopExportSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventExportSessionController
import com.intellij.openapi.project.Project

class StopExportGcEventSessionAction : StopExportSessionAction() {
    override fun stopSession(pid: Int, project: Project) {
        GcEventExportSessionController.getInstance(project).stopSession(pid)
    }

    override fun containsSession(pid: Int, project: Project) =
        GcEventExportSessionController.getInstance(project).containsSession(pid)
}