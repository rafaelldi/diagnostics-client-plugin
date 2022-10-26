package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.PredefinedProvider
import com.github.rafaelldi.diagnosticsclientplugin.generated.Trace
import com.github.rafaelldi.diagnosticsclientplugin.generated.TraceMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.TraceTabManager
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.process.ConsoleHighlighter
import com.intellij.execution.ui.ConsoleView
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
    val pid: Int,
    session: TraceMonitoringSession,
    private val manager: TraceTabManager,
    project: Project,
    lt: Lifetime
) : SimpleToolWindowPanel(false), Disposable {

    companion object {
        val TRACE_MONITORING_TAB: DataKey<TraceMonitoringTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.TraceMonitoringTab")
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
        val contentType =
            if (trace.provider == PredefinedProvider.Exceptions) ConsoleViewContentType.ERROR_OUTPUT
            else ConsoleViewContentType.NORMAL_OUTPUT

        consoleView.print(dateFormat.format(trace.timeStamp), contentType)
        consoleView.print(" [", contentType)
        consoleView.print(getProviderName(trace.provider), contentType)
        consoleView.print(" - ", contentType)
        consoleView.print(trace.eventName, contentType)
        consoleView.print("] ", contentType)
        consoleView.print(trace.content, contentType)
        consoleView.print("\n", contentType)
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