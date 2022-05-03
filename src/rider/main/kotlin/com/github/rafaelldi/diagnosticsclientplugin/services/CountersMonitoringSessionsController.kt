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
import com.jetbrains.rider.projectView.solution

@Service
class CountersMonitoringSessionsController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel

    init {
        hostModel.countersMonitoringSessions.view(projectComponentLifetime) { lt, _, session ->
            viewSession(lt, session)
        }
    }

    fun startSession(pid: Int, model: MonitorCountersModel) {
        val duration = if (model.stoppingType == StoppingType.AfterPeriod) model.duration else null
        val command = MonitorCountersCommand(pid, model.interval, model.providers, duration)
        val monitorTask = hostModel.monitorCounters.start(projectComponentLifetime, command)
        sessionStarted(pid)

        monitorTask
            .result
            .advise(projectComponentLifetime) { result ->
                if (result is RdTaskResult.Success) {
                    sessionFinished(pid)
                } else if (result is RdTaskResult.Fault) {
                    sessionFaulted(pid, result.error.reasonMessage)
                }
            }
    }

    fun stopSession(pid: Int) {
        hostModel.stopCountersMonitoring.fire(StopCountersMonitoringCommand(pid))
    }

    fun startExistingSession(pid: Int, duration: Int?) {
        val session = hostModel.countersMonitoringSessions[pid] ?: return
        val monitorTask = session.monitor.start(projectComponentLifetime, duration)
        sessionStarted(pid)

        monitorTask
            .result
            .advise(projectComponentLifetime) { result ->
                if (result is RdTaskResult.Success) {
                    sessionFinished(pid)
                } else if (result is RdTaskResult.Fault) {
                    sessionFaulted(pid, result.error.reasonMessage)
                }
            }
    }

    fun stopExistingSession(pid: Int) {
        hostModel.countersMonitoringSessions[pid]?.stop?.fire(Unit)
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