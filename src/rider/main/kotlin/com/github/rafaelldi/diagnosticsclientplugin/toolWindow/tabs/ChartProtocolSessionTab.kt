package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.ChartEvent
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.ChartSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterChartPanel
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.jetbrains.rd.util.lifetime.Lifetime

class ChartProtocolSessionTab(
    override val pid: Int,
    session: ChartProtocolSession,
    private val manager: ChartSessionTabManager,
    lt: Lifetime
): SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    private val panel: CounterChartPanel = CounterChartPanel()

    init {
        setContent(panel)
        initActionToolbar()

        session.eventReceived.advise(lt) { valueReceived(it) }
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.ChartSession") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.ChartSession.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    private fun valueReceived(value: ChartEvent) {
        panel.update(value)
    }

    override fun getData(dataId: String): Any? {
        if (SESSION_PROCESS_ID.`is`(dataId)) return pid
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}