package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.actions.notification.RevealFileAction
import com.github.rafaelldi.diagnosticsclientplugin.dialogs.CollectTracesModel
import com.github.rafaelldi.diagnosticsclientplugin.generated.CollectTracesCommand
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
class TraceCollectionSessionController(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val hostModel: DiagnosticsHostModel = project.solution.diagnosticsHostModel
    private val activeSessions: ConcurrentHashMap<Int, LifetimeDefinition> = ConcurrentHashMap()

    fun startSession(pid: Int, model: CollectTracesModel) {
        val sessionDefinition = createDefinitionForSession(pid) ?: return
        val filePath = Path(model.path, model.filename).pathString

        val command = CollectTracesCommand(pid, filePath)

        val collectTask = hostModel.collectTraces.start(sessionDefinition.lifetime, command)
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

    fun stopSession(pid: Int) {
        val sessionDefinition = activeSessions.remove(pid) ?: return
        sessionDefinition.terminate()
    }

    private fun sessionStarted(pid: Int) = Notification(
        "Diagnostics Client",
        "Traces collection started",
        "Session for process $pid started",
        NotificationType.INFORMATION
    )
        .notify(project)

    private fun sessionFinished(pid: Int, filePath: String) = Notification(
        "Diagnostics Client",
        "Traces collection finished",
        "Session for process $pid finished",
        NotificationType.INFORMATION
    )
        .addAction(RevealFileAction(filePath))
        .notify(project)

    private fun sessionFaulted(pid: Int, message: String) = Notification(
        "Diagnostics Client",
        "Traces collection for $pid faulted",
        message,
        NotificationType.ERROR
    )
        .notify(project)
}