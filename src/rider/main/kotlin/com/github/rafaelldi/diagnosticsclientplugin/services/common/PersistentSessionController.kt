package com.github.rafaelldi.diagnosticsclientplugin.services.common

import com.github.rafaelldi.diagnosticsclientplugin.common.persistentSessionAlreadyExists
import com.github.rafaelldi.diagnosticsclientplugin.common.persistentSessionFinished
import com.github.rafaelldi.diagnosticsclientplugin.common.persistentSessionStarted
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.PersistentModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.PersistentSession
import com.github.rafaelldi.diagnosticsclientplugin.topics.ArtifactListener
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IMutableViewableMap
import kotlinx.coroutines.Dispatchers
import java.time.Duration

abstract class PersistentSessionController<TSession : PersistentSession, TModel : PersistentModel>(protected val project: Project) :
    LifetimedService() {

    protected abstract val artifactType: String
    protected abstract val canBeOpened: Boolean
    protected abstract val sessions: IMutableViewableMap<Int, TSession>

    fun startSession(model: TModel) {
        val pid = model.selectedProcess?.pid ?: return

        if (sessions.contains(pid)) {
            sessionAlreadyExists(pid)
            return
        }

        val session = createSession(model)
        try {
            sessions.addUnique(serviceLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            sessionAlreadyExists(pid)
        }
    }

    protected abstract fun createSession(model: TModel): TSession

    fun stopSession(pid: Int) {
        sessions.remove(pid)
    }

    protected fun viewSession(pid: Int, session: TSession, lt: Lifetime) {
        if (session.duration != null) {
            val timerLifetime =
                lt.createTerminatedAfter(Duration.ofSeconds(session.duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (sessions.containsKey(pid)) {
                    sessions.remove(pid)
                }
            }
        }

        lt.bracketIfAlive(
            { sessionStarted(pid) },
            {
                sessionFinished(pid, session)
                project.messageBus.syncPublisher(ArtifactListener.TOPIC).artifactCreated(session.filePath)
            }
        )
    }

    private fun sessionAlreadyExists(pid: Int) = persistentSessionAlreadyExists(artifactType, pid, project)
    private fun sessionStarted(pid: Int) = persistentSessionStarted(artifactType, pid, project)
    private fun sessionFinished(pid: Int, session: TSession) =
        persistentSessionFinished(artifactType, pid, session.filePath, canBeOpened, project)
}