@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.services

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.DiagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.model.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSessionController
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSessionController
import com.github.rafaelldi.diagnosticsclientplugin.topics.HostListener
import com.github.rafaelldi.diagnosticsclientplugin.topics.HostProcessListener
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.util.application
import com.jetbrains.rd.framework.*
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.reactive.AddRemove
import com.jetbrains.rd.util.threading.coroutines.nextTrueValueAsync
import com.jetbrains.rdclient.protocol.RdDispatcher
import com.jetbrains.rider.util.NetUtils
import kotlinx.coroutines.delay
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.seconds

@Service(Service.Level.PROJECT)
class DiagnosticsHost(private val project: Project) : LifetimedService() {
    companion object {
        private const val INITIAL_PORT = 57800
        private val LOG = logger<DiagnosticsHost>()

        fun getInstance(project: Project) = project.service<DiagnosticsHost>()
    }

    private val agentLifetime = SequentialLifetimes(serviceLifetime)
    val isConnected get() = !agentLifetime.isTerminated
    var hostModel: DiagnosticsHostModel? = null
        private set

    suspend fun connectToAgent() {
        application.assertIsNonDispatchThread()

        if (!agentLifetime.isTerminated) return

        val toolService = DiagnosticsToolService.getInstance(project)

        val isGlobalToolReady = toolService.checkGlobalTool()
        if (!isGlobalToolReady) {
            LOG.warn("Diagnostics agent isn't ready")
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

    suspend fun findProcess(pathString: String): DotNetProcess? = hostModel?.let { model ->
        val path = Path(pathString)
        for (i in 0..9) {
            val values = model.processList.items.toList()
            val process = values.firstOrNull { it.second.filename != null && Path(it.second.filename!!) == path }
            if (process != null) {
                return@let DotNetProcess(process.first, process.second.processName)
            }

            delay(1.seconds)
        }

        return@let null
    }

    private suspend fun connectToProtocol(port: Int, lifetime: Lifetime): DiagnosticsHostModel = withUiContext {
        val dispatcher = RdDispatcher(lifetime)
        val wire = SocketWire.Client(lifetime, dispatcher, port)

        wire.connected.nextTrueValueAsync(lifetime).await()

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

        CounterSessionController.getInstance(project).subscribeTo(model.counterSessions, lifetime)
        GcEventSessionController.getInstance(project).subscribeTo(model.gcEventSessions, lifetime)
        TraceSessionController.getInstance(project).subscribeTo(model.traceSessions, lifetime)
        ChartSessionController.getInstance(project).subscribeTo(model.chartSessions, lifetime)
    }
}