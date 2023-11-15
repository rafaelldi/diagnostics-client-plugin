package com.github.rafaelldi.diagnosticsclientplugin.services.common

import com.github.rafaelldi.diagnosticsclientplugin.common.exportSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ExportSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.model.ExportSession
import com.github.rafaelldi.diagnosticsclientplugin.topics.ArtifactListener
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IMutableViewableMap
import com.jetbrains.rd.util.threading.coroutines.createTerminatedAfter
import kotlinx.coroutines.Dispatchers
import java.time.Duration

abstract class ExportSessionController<TSession : ExportSession, TModel : ExportSessionModel>(protected val project: Project) :
    LifetimedService() {

    protected abstract val artifactType: String
    protected abstract val canBeOpened: Boolean

    fun subscribeTo(sessions: IMutableViewableMap<Int, TSession>, lifetime: Lifetime) {
        sessions.view(lifetime) { sessionLifetime, pid, session ->
            viewSession(pid, session, sessionLifetime)
        }
    }

    protected abstract fun getSessions(): IMutableViewableMap<Int, TSession>?

    fun containsSession(pid: Int): Boolean? = getSessions()?.containsKey(pid)

    fun startSession(model: TModel) {
        val pid = model.selectedProcess?.pid ?: return
        val sessions = getSessions() ?: return

        if (sessions.contains(pid)) {
            return
        }

        val session = createSession(model)
        try {
            sessions.addUnique(serviceLifetime, pid, session)
        } catch (_: IllegalArgumentException) {
        }
    }

    protected abstract fun createSession(model: TModel): TSession

    fun stopSession(pid: Int) {
        getSessions()?.remove(pid)
    }

    private fun viewSession(pid: Int, session: TSession, lt: Lifetime) {
        val sessions = getSessions()

        if (session.duration != null && sessions != null) {
            val timerLifetime =
                lt.createTerminatedAfter(Duration.ofSeconds(session.duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (sessions.containsKey(pid)) {
                    sessions.remove(pid)
                }
            }
        }

        lt.onTermination {
            sessionFinished(pid, session)
            project.messageBus.syncPublisher(ArtifactListener.TOPIC).artifactCreated(session.exportFilePath)
        }
    }

    private fun sessionFinished(pid: Int, session: TSession) =
        exportSessionFinished(artifactType, pid, session.exportFilePath, canBeOpened, project)
}