package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.model.Trace
import com.github.rafaelldi.diagnosticsclientplugin.model.TraceProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.process.ConsoleHighlighter
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.rd.util.lifetime.Lifetime
import java.text.SimpleDateFormat

class TraceProtocolSessionTab(
    override val pid: Int,
    session: TraceProtocolSession,
    private val manager: TraceSessionTabManager,
    project: Project,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {
    companion object {
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

    private val panel = BorderLayoutPanel().apply {
        addToCenter(consoleView.component)
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

        consoleView.print(dateFormat.format(trace.timeStamp), providerContentType)
        consoleView.print(" [", providerContentType)
        consoleView.print(getProviderName(trace.provider), providerContentType)
        consoleView.print(" - ", providerContentType)
        consoleView.print(trace.eventName, providerContentType)
        consoleView.print("] ", providerContentType)
        consoleView.print(trace.content, providerContentType)
        consoleView.print("\n", providerContentType)
    }

    private fun getProviderName(provider: PredefinedProvider): String =  when (provider)
    {
        PredefinedProvider.Http -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.http")
        PredefinedProvider.AspNet -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.asp.net.core")
        PredefinedProvider.EF -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.ef.core")
        PredefinedProvider.Exceptions -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.exceptions")
        PredefinedProvider.Threads -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.threads")
        PredefinedProvider.Contentions -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.contentions")
        PredefinedProvider.Tasks -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.tasks")
        PredefinedProvider.Loader -> DiagnosticsClientBundle.message("live.trace.tab.predefined.provider.loader")
    }

    override fun getData(dataId: String): Any? {
        if (SESSION_PROCESS_ID.`is`(dataId)) return pid
        return super.getData(dataId)
    }

    override fun dispose() {
        Disposer.dispose(consoleView)
        manager.tabClosed(pid)
    }
}