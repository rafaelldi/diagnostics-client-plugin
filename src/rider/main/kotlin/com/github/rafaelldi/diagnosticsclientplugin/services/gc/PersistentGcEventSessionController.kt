package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.GcEventModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.PersistentGcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.PersistentSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class PersistentGcEventSessionController(project: Project) :
    PersistentSessionController<PersistentGcEventSession, GcEventModel>(project) {
    companion object {
        fun getInstance(project: Project): PersistentGcEventSessionController = project.service()
        private const val GC_EVENTS = "GC events"
    }

    override val artifactType = GC_EVENTS
    override val canBeOpened = true
    override val sessions = project.solution.diagnosticsHostModel.persistentGcEventSessions

    init {
        sessions.view(serviceLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    override fun createSession(model: GcEventModel): PersistentGcEventSession {
        val filePath = Path(model.path, "${model.filename}.csv").pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        return PersistentGcEventSession(duration, filePath)
    }
}