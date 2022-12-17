package com.github.rafaelldi.diagnosticsclientplugin.services.common

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitoringModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.MonitoringSession
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.util.createTerminatedAfter
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IMutableViewableMap
import com.jetbrains.rd.util.reactive.whenTrue
import kotlinx.coroutines.Dispatchers
import java.time.Duration

abstract class MonitoringSessionController<TSession : MonitoringSession, TModel : MonitoringModel>(project: Project) :
    ProtocolSubscribedProjectComponent(project) {

    protected abstract val sessions: IMutableViewableMap<Int, TSession>

    fun startSession(pid: Int, model: TModel) {
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
            sessions.addUnique(projectComponentLifetime, pid, session)
        } catch (e: IllegalArgumentException) {
            // do nothing
        }
    }

    protected abstract fun createSession(model: TModel): TSession

    fun startExistingSession(pid: Int, stoppingType: StoppingType, duration: Int) {
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

    fun stopSession(pid: Int) {
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