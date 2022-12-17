package com.github.rafaelldi.diagnosticsclientplugin.services.common

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectionModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectionSession
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IMutableViewableMap
import kotlinx.coroutines.Dispatchers
import java.time.Duration

abstract class CollectionSessionController<TSession : CollectionSession, TModel : CollectionModel>(project: Project) :
    ProtocolSubscribedProjectComponent(project) {

    protected abstract val sessions: IMutableViewableMap<Int, TSession>

    init {
        sessions.view(projectComponentLifetime) { lt, pid, session ->
            viewSession(pid, session, lt)
        }
    }

    fun startSession(pid: Int, model: TModel) {
        if (sessions.contains(pid)) {
            sessionAlreadyExists(pid)
            return
        }

        val session = createSession(model)
        try {
            sessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            sessionAlreadyExists(pid)
        }
    }

    protected abstract fun createSession(model: TModel): TSession

    fun stopSession(pid: Int) {
        sessions.remove(pid)
    }

    private fun viewSession(pid: Int, session: TSession, lt: Lifetime) {
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
            { sessionFinished(pid, session) }
        )
    }

    protected abstract fun sessionAlreadyExists(pid: Int)
    protected abstract fun sessionStarted(pid: Int)
    protected abstract fun sessionFinished(pid: Int, session: TSession)
}