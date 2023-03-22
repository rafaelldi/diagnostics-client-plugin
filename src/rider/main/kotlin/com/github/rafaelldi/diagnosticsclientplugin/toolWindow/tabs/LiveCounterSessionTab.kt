package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.Counter
import com.github.rafaelldi.diagnosticsclientplugin.generated.LiveCounterSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterTable
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IViewableMap
import java.awt.BorderLayout
import javax.swing.JPanel

class LiveCounterSessionTab(
    override val pid: Int,
    session: LiveCounterSession,
    private val manager: CounterTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    companion object {
        val COUNTER_MONITORING_TAB: DataKey<LiveCounterSessionTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.CounterMonitoringTab")
    }

    private val table: CounterTable

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        table = CounterTable()
        add(JBScrollPane(table))
    }

    init {
        setContent(panel)
        initActionToolbar()

        session.counters.advise(lt) { countersCollectionChanged(it) }
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

    private fun countersCollectionChanged(event: IViewableMap.Event<String, Counter>) {
        when (event) {
            is IViewableMap.Event.Add -> table.add(event.key, event.newValue)
            is IViewableMap.Event.Update -> table.update(event.key, event.newValue)
            is IViewableMap.Event.Remove -> table.remove(event.key)
        }
    }

    override fun getData(dataId: String): Any? {
        if (COUNTER_MONITORING_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}