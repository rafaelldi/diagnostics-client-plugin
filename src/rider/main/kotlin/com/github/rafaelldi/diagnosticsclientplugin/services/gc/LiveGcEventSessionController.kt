package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionNotFound
import com.github.rafaelldi.diagnosticsclientplugin.common.monitoringSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.LiveSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution

@Service
class LiveGcEventSessionController(project: Project) :
    LiveSessionController<LiveGcEventSession, GcEventModel>(project) {
    companion object {
        fun getInstance(project: Project): LiveGcEventSessionController = project.service()
        private const val GC_EVENTS = "GC events"
    }

    override val sessions = project.solution.diagnosticsHostModel.liveGcEventSessions

    init {
        sessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: GcEventModel): LiveGcEventSession {
        return LiveGcEventSession()
    }

    override fun sessionNotFound(pid: Int) = monitoringSessionNotFound(GC_EVENTS, pid, project)
    override fun sessionStarted(pid: Int) = monitoringSessionStarted(GC_EVENTS, pid, project)
    override fun sessionFinished(pid: Int) = monitoringSessionFinished(GC_EVENTS, pid, project)
}