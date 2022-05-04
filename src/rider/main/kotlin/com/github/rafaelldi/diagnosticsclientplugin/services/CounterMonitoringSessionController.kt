package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.dialogs.MonitorCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.generated.*
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsTabsManager
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.RdTaskResult
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rider.projectView.solution
import java.util.concurrent.ConcurrentHashMap

@Service
class CounterMonitoringSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel
    private val definitions: ConcurrentHashMap<Int, LifetimeDefinition> = ConcurrentHashMap()

    init {
        hostModel.countersMonitoringSessions.view(projectComponentLifetime) { lt, _, session ->
            viewSession(lt, session)
        }
    }

    fun startSession(pid: Int, model: MonitorCountersModel) {
        val sessionDefinition = createDefinitionForSession(pid) ?: return

        val duration = if (model.stoppingType == StoppingType.AfterPeriod) model.duration else null
        val command = MonitorCountersCommand(pid, model.interval, model.providers, duration)

        val monitorTask = hostModel.monitorCounters.start(sessionDefinition.lifetime, command)
        sessionStarted(pid)

        monitorTask
            .result
            .advise(projectComponentLifetime) { result ->
                when (result) {
                    is RdTaskResult.Success -> {
                        definitions.remove(pid)
                        sessionFinished(pid)
                    }
                    is RdTaskResult.Cancelled -> {
                        sessionFinished(pid)
                    }
                    is RdTaskResult.Fault -> {
                        sessionFaulted(pid, result.error.reasonMessage)
                    }
                }
            }
    }

    fun startExistingSession(pid: Int, duration: Int?) {
        val sessionDefinition = createDefinitionForSession(pid) ?: return

        val session = hostModel.countersMonitoringSessions[pid] ?: return
        val monitorTask = session.monitor.start(sessionDefinition.lifetime, duration)
        sessionStarted(pid)

        monitorTask
            .result
            .advise(projectComponentLifetime) { result ->
                when (result) {
                    is RdTaskResult.Success -> {
                        definitions.remove(pid)
                        sessionFinished(pid)
                    }
                    is RdTaskResult.Cancelled -> {
                        sessionFinished(pid)
                    }
                    is RdTaskResult.Fault -> {
                        sessionFaulted(pid, result.error.reasonMessage)
                    }
                }
            }
    }

    private fun createDefinitionForSession(pid: Int): LifetimeDefinition? {
        if (definitions.containsKey(pid)) {
            return null
        }

        val sessionDefinition = projectComponentLifetime.createNested()
        val currentDefinition = definitions.putIfAbsent(pid, sessionDefinition)
        if (currentDefinition != null) {
            return null
        }

        return sessionDefinition
    }

    fun stopSession(pid: Int) = stopSessionCore(pid)

    fun stopExistingSession(pid: Int) = stopSessionCore(pid)

    private fun stopSessionCore(pid: Int) {
        val sessionDefinition = definitions.remove(pid) ?: return
        sessionDefinition.terminate()
    }

    private fun viewSession(lt: Lifetime, session: CountersMonitoringSession) {
        val tabsManager = project.service<DiagnosticsTabsManager>()
        tabsManager.createCountersTab(lt, session)
    }

    private fun sessionStarted(pid: Int) = Notification(
        "Diagnostics Client",
        "Counters monitoring started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int) = Notification(
        "Diagnostics Client",
        "Counters monitoring finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFaulted(pid: Int, message: String) = Notification(
        "Diagnostics Client",
        "Counters monitoring for $pid faulted",
        message,
        NotificationType.ERROR
    )
        .notify(project)
}