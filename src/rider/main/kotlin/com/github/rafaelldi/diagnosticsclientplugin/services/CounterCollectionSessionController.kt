package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.OpenFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectCountersModel
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CounterFileFormat
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.StoppingType
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.map
import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectCountersCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.framework.RdTaskResult
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rider.projectView.solution
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class CounterCollectionSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel
    private val activeSessions: ConcurrentHashMap<Int, LifetimeDefinition> = ConcurrentHashMap()

    fun startSession(pid: Int, model: CollectCountersModel) {
        val sessionDefinition = createDefinitionForSession(pid) ?: return

        val filePath = calculateFilePath(model)
        val duration = if (model.stoppingType == StoppingType.AfterPeriod) model.duration else null
        val command = CollectCountersCommand(
            pid,
            filePath,
            model.format.map(),
            model.interval,
            model.providers,
            duration
        )

        val collectTask = hostModel.collectCounters.start(sessionDefinition.lifetime, command)
        sessionStarted(pid)

        collectTask
            .result
            .advise(projectComponentLifetime) { result ->
                when (result) {
                    is RdTaskResult.Success -> {
                        activeSessions.remove(pid)
                        sessionFinished(pid, filePath)
                    }
                    is RdTaskResult.Cancelled -> {
                        sessionFinished(pid, filePath)
                    }
                    is RdTaskResult.Fault -> {
                        sessionFaulted(pid, result.error.reasonMessage)
                    }
                }
            }
    }

    private fun createDefinitionForSession(pid: Int): LifetimeDefinition? {
        if (activeSessions.containsKey(pid)) {
            return null
        }

        val sessionDefinition = projectComponentLifetime.createNested()
        val currentDefinition = activeSessions.putIfAbsent(pid, sessionDefinition)
        if (currentDefinition != null) {
            return null
        }

        return sessionDefinition
    }

    private fun calculateFilePath(model: CollectCountersModel): String {
        val filename = when (model.format) {
            CounterFileFormat.Csv -> {
                if (model.filename.endsWith(".csv"))
                    model.filename
                else
                    "${model.filename}.csv"
            }
            CounterFileFormat.Json -> {
                if (model.filename.endsWith(".json"))
                    model.filename
                else
                    "${model.filename}.json"
            }
        }

        return Path(model.path, filename).pathString
    }

    fun stopSession(pid: Int) {
        val sessionDefinition = activeSessions.remove(pid) ?: return
        sessionDefinition.terminate()
    }

    private fun sessionStarted(pid: Int) = Notification(
        "Diagnostics Client",
        "Counters collection started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int, filePath: String) = Notification(
        "Diagnostics Client",
        "Counters collection finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(OpenFileAction(filePath))
        .notify(project)

    private fun sessionFaulted(pid: Int, message: String) = Notification(
        "Diagnostics Client",
        "Counters collection for $pid faulted",
        message,
        NotificationType.ERROR
    )
        .notify(project)
}