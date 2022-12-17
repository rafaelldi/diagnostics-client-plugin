package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionAlreadyExists
import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventCollectionSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.common.CollectionSessionController
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solution
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class GcEventCollectionSessionController(project: Project) :
    CollectionSessionController<GcEventCollectionSession, CollectGcEventsModel>(project) {
    companion object {
        fun getInstance(project: Project): GcEventCollectionSessionController = project.service()
        private const val GC_EVENTS = "GC events"
    }

    override val sessions = project.solution.diagnosticsHostModel.gcEventCollectionSessions

    override fun createSession(model: CollectGcEventsModel): GcEventCollectionSession {
        val filePath = Path(model.path, "${model.filename}.csv").pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        return GcEventCollectionSession(duration, filePath)
    }

    override fun sessionAlreadyExists(pid: Int) = collectionSessionAlreadyExists(GC_EVENTS, pid, project)
    override fun sessionStarted(pid: Int) = collectionSessionStarted(GC_EVENTS, pid, project)
    override fun sessionFinished(pid: Int, session: GcEventCollectionSession) =
        collectionSessionFinished(GC_EVENTS, pid, session.filePath, true, project)
}