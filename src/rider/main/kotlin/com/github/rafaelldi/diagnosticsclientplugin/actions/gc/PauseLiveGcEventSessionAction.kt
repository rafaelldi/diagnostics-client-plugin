package com.github.rafaelldi.diagnosticsclientplugin.actions.gc

import com.github.rafaelldi.diagnosticsclientplugin.actions.common.PauseLiveSessionAction
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs.GcEventProtocolSessionTab
import com.intellij.openapi.project.Project

class PauseLiveGcEventSessionAction : PauseLiveSessionAction<GcEventProtocolSession, GcEventProtocolSessionTab>() {
    override fun pauseSession(pid: Int, project: Project) {
        GcEventProtocolSessionController.getInstance(project).pauseSession(pid)
    }

    override fun getSession(pid: Int, project: Project) =
        GcEventProtocolSessionController.getInstance(project).getSession(pid)
}