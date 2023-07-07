@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.model.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.LiveChartSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.ExportCounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.LiveCounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.ExportGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.LiveGcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.ExportTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.LiveTraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.topics.HostListener
import com.github.rafaelldi.diagnosticsclientplugin.topics.HostProcessListener
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.withBackgroundProgress
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.rd.util.withUiContext
import com.jetbrains.rd.framework.*
import com.jetbrains.rd.framework.util.nextTrueValue
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.reactive.AddRemove
import com.jetbrains.rdclient.protocol.RdDispatcher
import com.jetbrains.rider.util.NetUtils

@Service
class DiagnosticsHost(private val project: Project) : LifetimedService() {
    companion object {
        private const val INITIAL_PORT = 57800

        fun getInstance(project: Project) = project.service<DiagnosticsHost>()
    }

    private val agentLifetime = SequentialLifetimes(serviceLifetime)
    val isConnected get() = !agentLifetime.isTerminated
    var hostModel: DiagnosticsHostModel? = null
        private set

    suspend fun connectToAgent() {
        if (!agentLifetime.isTerminated) return

        val toolService = DiagnosticsToolService.getInstance(project)

        val isGlobalToolReady = withUiContext { toolService.checkGlobalTool() }
        if (!isGlobalToolReady) {
            return
        }

        withBackgroundProgress(project, DiagnosticsClientBundle.message("diagnostics.host.connect.progress")) {
            val lifetimeDefinition = agentLifetime.next()

            val port = NetUtils.findFreePort(INITIAL_PORT)

            val processHandler = DiagnosticsToolService.getInstance(project).run(port)
            lifetimeDefinition.onTermination {
                if (!processHandler.isProcessTerminating && !processHandler.isProcessTerminated) {
                    processHandler.killProcess()
                }
            }
            processHandler.addProcessListener(object : ProcessListener {
                override fun processTerminated(event: ProcessEvent) {
                    lifetimeDefinition.executeIfAlive {
                        lifetimeDefinition.terminate(true)
                    }
                }
            }, lifetimeDefinition.createNestedDisposable())
            processHandler.startNotify()

            val model = connectToProtocol(port, lifetimeDefinition.lifetime)

            subscribeToModel(model, lifetimeDefinition.lifetime)

            hostModel = model
        }
    }

    fun disconnectFromAgent() {
        if (agentLifetime.isTerminated) return
        agentLifetime.terminateCurrent()
        hostModel = null
        project.messageBus.syncPublisher(HostListener.TOPIC).hostDisconnected()
    }

    private suspend fun connectToProtocol(port: Int, lifetime: Lifetime): DiagnosticsHostModel = withUiContext {
        val dispatcher = RdDispatcher(lifetime)
        val wire = SocketWire.Client(lifetime, dispatcher, port)

        wire.connected.nextTrueValue(lifetime)

        val protocol = Protocol(
            "Diagnostics Host to Agent",
            Serializers(),
            Identities(IdKind.Client),
            dispatcher,
            wire,
            lifetime
        )

        return@withUiContext protocol.diagnosticsHostModel
    }

    private suspend fun subscribeToModel(model: DiagnosticsHostModel, lifetime: Lifetime) = withUiContext {
        model.processList.items.adviseAddRemove(lifetime) { action, pid, processInfo ->
            when (action) {
                AddRemove.Add -> project.messageBus.syncPublisher(HostProcessListener.TOPIC)
                    .processAdded(pid, processInfo)

                AddRemove.Remove -> project.messageBus.syncPublisher(HostProcessListener.TOPIC)
                    .processRemoved(pid, processInfo)
            }
        }

        ExportCounterSessionController.getInstance(project).subscribeTo(model.persistentCounterSessions, lifetime)
        ExportGcEventSessionController.getInstance(project).subscribeTo(model.persistentGcEventSessions, lifetime)
        ExportTraceSessionController.getInstance(project).subscribeTo(model.persistentTraceSessions, lifetime)

        LiveCounterSessionController.getInstance(project).subscribeTo(model.liveCounterSessions, lifetime)
        LiveGcEventSessionController.getInstance(project).subscribeTo(model.liveGcEventSessions, lifetime)
        LiveTraceSessionController.getInstance(project).subscribeTo(model.liveTraceSessions, lifetime)
        LiveChartSessionController.getInstance(project).subscribeTo(model.liveChartSessions, lifetime)
    }
}