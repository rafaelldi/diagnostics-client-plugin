package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventProtocolSessionTab
import com.intellij.openapi.project.Project

class PauseGcEventSessionAction : PauseSessionAction<GcEventSession, GcEventProtocolSessionTab>() {
    override fun pauseSession(pid: Int, project: Project) {
        GcEventSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        GcEventSessionController.getInstance(project).getSession(pid)
}