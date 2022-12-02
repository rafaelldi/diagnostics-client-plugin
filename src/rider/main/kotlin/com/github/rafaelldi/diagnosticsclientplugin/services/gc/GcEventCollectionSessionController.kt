package com.github.rafaelldi.diagnosticsclientplugin.services.gc

import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionAlreadyExists
import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.collectionSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectGcEventsModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventCollectionSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import java.time.Duration
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class GcEventCollectionSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): GcEventCollectionSessionController = project.service()
        private const val GC_EVENTS = "GC events"
    }

    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.gcEventCollectionSessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: CollectGcEventsModel) {
        if (hostModel.gcEventCollectionSessions.contains(pid)) {
            collectionSessionAlreadyExists(GC_EVENTS, pid, project)
            return
        }

        val filePath = Path(model.path, "${model.filename}.csv").pathString
        val duration =
            if (model.stoppingType == StoppingType.AfterPeriod) model.duration
            else null

        val session = GcEventCollectionSession(filePath, duration)

        try {
            hostModel.gcEventCollectionSessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            collectionSessionAlreadyExists(GC_EVENTS, pid, project)
        }
    }

    fun stopSession(pid: Int) {
        hostModel.gcEventCollectionSessions.remove(pid)
    }

    private fun viewSession(pid: Int, session: GcEventCollectionSession, lt: Lifetime) {
        if (session.duration != null) {
            val timerLifetime =
                lt.createTerminatedAfter(Duration.ofSeconds(session.duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (hostModel.gcEventCollectionSessions.containsKey(pid)) {
                    hostModel.gcEventCollectionSessions.remove(pid)
                }
            }
        }

        lt.bracketIfAlive(
            { collectionSessionStarted(GC_EVENTS, pid, project) },
            { collectionSessionFinished(GC_EVENTS, pid, session.filePath, true, project) }
        )
    }
}