package com.github.rafaelldi.diagnosticsclientplugin.services.common

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.LiveModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveSession
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IMutableViewableMap
import com.jetbrains.rd.util.reactive.whenTrue
import kotlinx.coroutines.Dispatchers
import java.time.Duration

abstract class LiveSessionController<TSession : LiveSession, TModel : LiveModel>(protected val project: Project) :
    LifetimedService() {

    protected abstract val sessions: IMutableViewableMap<Int, TSession>

    fun startSession(model: TModel) {
        val pid = model.selectedProcess?.pid ?: return

        if (!sessions.contains(pid)) {
            createNewSession(pid, model)
        }

        startExistingSession(pid, model.stoppingType, model.duration)
    }

    private fun createNewSession(pid: Int, model: TModel) {
        if (sessions.contains(pid)) {
            return
        }

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
        val session = sessions[pid]
        if (session == null) {
            sessionNotFound(pid)
            return
        }

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
        val session = sessions[pid]
        if (session == null) {
            sessionNotFound(pid)
            return
        }

        if (session.active.valueOrNull == true) {
            session.active.set(false)
        }
    }

    fun closeSession(pid: Int) {
        sessions.remove(pid)
    }

    protected fun viewSession(pid: Int, session: TSession, lifetime: Lifetime) {
        session.active.whenTrue(lifetime) { lt -> viewActiveStatus(pid, session, lt) }
    }

    private fun viewActiveStatus(pid: Int, session: TSession, lifetime: Lifetime) {
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

        lifetime.bracketIfAlive(
            { sessionStarted(pid) },
            { sessionFinished(pid) }
        )
    }

    protected abstract fun sessionNotFound(pid: Int)
    protected abstract fun sessionStarted(pid: Int)
    protected abstract fun sessionFinished(pid: Int)
}