package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveTraceSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.generated.Trace
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceTabManager
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.process.ConsoleHighlighter
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import com.jetbrains.rd.util.lifetime.Lifetime
import java.awt.BorderLayout
import java.text.SimpleDateFormat
import javax.swing.JPanel

class TraceMonitoringTab(
    override val pid: Int,
    session: LiveTraceSession,
    private val manager: TraceTabManager,
    project: Project,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {
    companion object {
        val TRACE_MONITORING_TAB: DataKey<TraceMonitoringTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.TraceMonitoringTab")

        private val HTTP_OUTPUT = ConsoleViewContentType("HTTP_OUTPUT", ConsoleHighlighter.BLUE)
        private val ASPNET_OUTPUT = ConsoleViewContentType("ASPNET_OUTPUT", ConsoleHighlighter.MAGENTA)
        private val EF_OUTPUT = ConsoleViewContentType("EF_OUTPUT", ConsoleHighlighter.MAGENTA_BRIGHT)
        private val EXCEPTIONS_OUTPUT = ConsoleViewContentType("EXCEPTIONS_OUTPUT", ConsoleHighlighter.RED)
        private val THREADS_OUTPUT = ConsoleViewContentType("THREADS_OUTPUT", ConsoleHighlighter.CYAN)
        private val CONTENTIONS_OUTPUT = ConsoleViewContentType("CONTENTIONS_OUTPUT", ConsoleHighlighter.YELLOW)
        private val TASKS_OUTPUT = ConsoleViewContentType("TASKS_OUTPUT", ConsoleHighlighter.GRAY)
        private val LOADER_OUTPUT = ConsoleViewContentType("LOADER_OUTPUT", ConsoleHighlighter.GREEN)
    }

    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    private val consoleView: ConsoleViewImpl =
        TextConsoleBuilderFactory.getInstance()
            .createBuilder(project)
            .apply { setViewer(true) }
            .console as ConsoleViewImpl

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        add(consoleView.component, BorderLayout.CENTER)
    }

    init {
        consoleView.clear()
        setContent(panel)
        initActionToolbar()

        session.traceReceived.advise(lt) { traceReceived(it) }

        Disposer.register(this, consoleView)
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.TraceSession") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.TraceSession.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    private fun traceReceived(trace: Trace) {
        val providerContentType = when(trace.provider) {
            PredefinedProvider.Http -> HTTP_OUTPUT
            PredefinedProvider.AspNet -> ASPNET_OUTPUT
            PredefinedProvider.EF -> EF_OUTPUT
            PredefinedProvider.Exceptions -> EXCEPTIONS_OUTPUT
            PredefinedProvider.Threads -> THREADS_OUTPUT
            PredefinedProvider.Contentions -> CONTENTIONS_OUTPUT
            PredefinedProvider.Tasks -> TASKS_OUTPUT
            PredefinedProvider.Loader -> LOADER_OUTPUT
        }

        consoleView.print(dateFormat.format(trace.timeStamp), ConsoleViewContentType.NORMAL_OUTPUT)
        consoleView.print(" [", ConsoleViewContentType.NORMAL_OUTPUT)
        consoleView.print(getProviderName(trace.provider), providerContentType)
        consoleView.print(" - ", providerContentType)
        consoleView.print(trace.eventName, providerContentType)
        consoleView.print("] ", ConsoleViewContentType.NORMAL_OUTPUT)
        consoleView.print(trace.content, ConsoleViewContentType.NORMAL_OUTPUT)
        consoleView.print("\n", ConsoleViewContentType.NORMAL_OUTPUT)
    }

    private fun getProviderName(provider: PredefinedProvider): String =  when (provider)
    {
        PredefinedProvider.Http -> "HTTP"
        PredefinedProvider.AspNet -> "ASP NET CORE"
        PredefinedProvider.EF -> "EF CORE"
        PredefinedProvider.Exceptions -> "EXCEPTIONS"
        PredefinedProvider.Threads -> "THREADS"
        PredefinedProvider.Contentions -> "CONTENTIONS"
        PredefinedProvider.Tasks -> "TASKS"
        PredefinedProvider.Loader -> "LOADER"
    }

    override fun getData(dataId: String): Any? {
        if (TRACE_MONITORING_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        consoleView.dispose()
        manager.tabClosed(pid)
    }
}