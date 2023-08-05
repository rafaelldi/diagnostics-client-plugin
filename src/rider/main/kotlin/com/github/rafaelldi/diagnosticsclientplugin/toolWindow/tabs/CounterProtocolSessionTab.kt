package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.Counter
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterTable
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import java.awt.BorderLayout
import javax.swing.JPanel

class CounterProtocolSessionTab(
    override val pid: Int,
    session: CounterProtocolSession,
    private val manager: CounterSessionTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    companion object {
        val COUNTER_MONITORING_TAB: DataKey<CounterProtocolSessionTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.CounterMonitoringTab")
    }

    private val table: CounterTable = CounterTable()

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        add(JBScrollPane(table))
    }

    init {
        setContent(panel)
        initActionToolbar()

        session.counterReceived.advise(lt) { counterReceived(it) }
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.CounterSession") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.CountersSession.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    private fun counterReceived(counter: Counter) {
        table.addOrUpdate(counter)
    }

    override fun getData(dataId: String): Any? {
        if (COUNTER_MONITORING_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}