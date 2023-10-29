package com.github.rafaelldi.diagnosticsclientplugin.services.common

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.ProtocolSessionModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.model.ProtocolSession
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IMutableViewableMap
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rd.util.threading.coroutines.createTerminatedAfter
import kotlinx.coroutines.Dispatchers
import java.time.Duration

abstract class ProtocolSessionController<TSession : ProtocolSession, TModel : ProtocolSessionModel>(protected val project: Project) :
    LifetimedService() {

    protected abstract val artifactType: String

    fun subscribeTo(sessions: IMutableViewableMap<Int, TSession>, lifetime: Lifetime) {
        sessions.view(lifetime) { sessionLifetime, pid, session ->
            viewSession(session, sessionLifetime)
            addSessionTab(pid, session, lifetime)
        }
    }

    protected abstract fun getSessions(): IMutableViewableMap<Int, TSession>?

    fun getSession(pid: Int): TSession? = getSessions()?.get(pid)

    fun startSession(model: TModel) {
        val pid = model.selectedProcess?.pid ?: return
        val sessions = getSessions() ?: return

        if (!sessions.contains(pid)) {
            createNewSession(pid, model)
        }

        startExistingSession(pid, model.stoppingType, model.duration)
    }

    private fun createNewSession(pid: Int, model: TModel) {
        val sessions = getSessions() ?: return

        if (sessions.contains(pid)) return

        val session = createSession(model)
        try {
            sessions.addUnique(serviceLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            // do nothing
        }
    }

    protected abstract fun createSession(model: TModel): TSession

    fun resumeSession(pid: Int, stoppingType: StoppingType, duration: Int) {
        startExistingSession(pid, stoppingType, duration)
    }

    private fun startExistingSession(pid: Int, stoppingType: StoppingType, duration: Int) {
        val sessions = getSessions() ?: return
        val session = sessions[pid] ?: return

        if (session.active.valueOrNull == true) {
            return
        }

        if (stoppingType == StoppingType.AfterPeriod) {
            session.duration.set(duration)
        } else {
            session.duration.set(null)
        }

        session.active.set(true)
    }

    fun pauseSession(pid: Int) {
        val sessions = getSessions() ?: return
        val session = sessions[pid] ?: return

        if (session.active.valueOrNull == true) {
            session.active.set(false)
        }
    }

    fun closeSession(pid: Int) {
        getSessions()?.remove(pid)
    }

    private fun viewSession(session: TSession, lifetime: Lifetime) {
        session.active.whenTrue(lifetime) { lt -> viewActiveStatus(session, lt) }
    }

    protected abstract fun addSessionTab(pid: Int, session: TSession, sessionLifetime: Lifetime)

    private fun viewActiveStatus(session: TSession, lifetime: Lifetime) {
        val duration = session.duration.value
        if (duration != null) {
            val timerLifetime =
                lifetime.createTerminatedAfter(Duration.ofSeconds(duration.toLong()), Dispatchers.Main)
            timerLifetime.onTermination {
                if (session.active.valueOrNull == true) {
                    session.active.set(false)
                }
            }
        }
    }
}