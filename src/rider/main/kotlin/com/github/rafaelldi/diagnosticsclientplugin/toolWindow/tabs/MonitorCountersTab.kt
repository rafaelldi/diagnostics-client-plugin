package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.Counter
import com.github.rafaelldi.diagnosticsclientplugin.generated.CountersMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.generated.RemoveCountersMonitoringSessionCommand
import com.github.rafaelldi.diagnosticsclientplugin.generated.diagnosticsHostModel
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsClientDataKeys
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsTabsManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterTableComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IViewableMap
import com.jetbrains.rider.projectView.solution
import java.awt.BorderLayout
import javax.swing.JPanel

class MonitorCountersTab(
    private val project: Project,
    private val session: CountersMonitoringSession,
    private val tabsManager: DiagnosticsTabsManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), Disposable {

    private val table: CounterTableComponent

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        table = CounterTableComponent()
        add(JBScrollPane(table))
    }

    init {
        setContent(panel)
        initActionToolbar()

        session.counters.advise(lt) { handle(it) }
        session.started.advise(lt) { sessionStarted() }
        session.stopped.advise(lt) { sessionFinished() }
    }

    fun getSessionPid() = session.pid

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.CountersSession") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.CountersSession.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    private fun handle(event: IViewableMap.Event<String, Counter>) {
        when (event) {
            is IViewableMap.Event.Add -> table.add(event.key, event.newValue)
            is IViewableMap.Event.Update -> table.update(event.key, event.newValue)
            is IViewableMap.Event.Remove -> table.remove(event.key)
        }
    }

    private fun sessionStarted() {
        tabsManager.activateCountersSessionTab(session.pid)
        Notification(
            "Diagnostics Client",
            "Counters monitoring started",
            "Session for process ${session.pid} started",
            NotificationType.INFORMATION
        )
            .notify(project)
    }

    private fun sessionFinished() {
        tabsManager.deactivateCountersSessionTab(session.pid)
        Notification(
            "Diagnostics Client",
            "Counters monitoring finished",
            "Session for process ${session.pid} finished",
            NotificationType.INFORMATION
        )
            .notify(project)
    }

    override fun getData(dataId: String): Any? {
        if (DiagnosticsClientDataKeys.MONITOR_COUNTERS_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        project.solution.diagnosticsHostModel.removeCountersMonitoringSession.fire(
            RemoveCountersMonitoringSessionCommand(session.pid)
        )
    }
}