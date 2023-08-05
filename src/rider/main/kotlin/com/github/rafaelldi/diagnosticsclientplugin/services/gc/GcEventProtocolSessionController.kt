package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.services.DiagnosticsHost
import com.github.rafaelldi.diagnosticsclientplugin.services.common.ProtocolSessionController
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.GcEventSessionTabManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime

@Service
class GcEventProtocolSessionController(project: Project) :
    ProtocolSessionController<GcEventProtocolSession, GcEventSessionModel>(project) {
    companion object {
        fun getInstance(project: Project): GcEventProtocolSessionController = project.service()
        private const val GC_EVENTS = "GC events"
    }

    override val artifactType = GC_EVENTS

    override fun getSessions() = DiagnosticsHost.getInstance(project).hostModel?.gcEventProtocolSessions

    override fun addSessionTab(pid: Int, session: GcEventProtocolSession, sessionLifetime: Lifetime) {
        GcEventSessionTabManager.getInstance(project).addSessionTab(sessionLifetime, pid, session)
    }

    override fun createSession(model: GcEventSessionModel) = GcEventProtocolSession()
}