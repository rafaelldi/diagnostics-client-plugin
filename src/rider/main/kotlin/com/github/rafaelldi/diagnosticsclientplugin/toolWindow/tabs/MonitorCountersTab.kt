package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.Counter
import com.github.rafaelldi.diagnosticsclientplugin.generated.CounterMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterTableComponent
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

class MonitorCountersTab(
    val pid: Int,
    session: CounterMonitoringSession,
    private val manager: CounterTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), Disposable {

    companion object {
        val MONITOR_COUNTERS_TAB: DataKey<MonitorCountersTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.MonitorCountersTab")
    }

    private val table: CounterTableComponent

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        table = CounterTableComponent()
        add(JBScrollPane(table))
    }

    init {
        setContent(panel)
        initActionToolbar()

        session.counters.advise(lt) { countersCollectionChanged(it) }
    }

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

    private fun countersCollectionChanged(event: IViewableMap.Event<String, Counter>) {
        when (event) {
            is IViewableMap.Event.Add -> table.add(event.key, event.newValue)
            is IViewableMap.Event.Update -> table.update(event.key, event.newValue)
            is IViewableMap.Event.Remove -> table.remove(event.key)
        }
    }

    override fun getData(dataId: String): Any? {
        if (MONITOR_COUNTERS_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}