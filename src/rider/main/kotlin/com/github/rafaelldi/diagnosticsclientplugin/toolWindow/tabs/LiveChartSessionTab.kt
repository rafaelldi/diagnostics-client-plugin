package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.ChartValue
import com.github.rafaelldi.diagnosticsclientplugin.model.LiveChartSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.ChartSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterChartPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.jetbrains.rd.util.lifetime.Lifetime

class LiveChartSessionTab(
    override val pid: Int,
    session: LiveChartSession,
    private val manager: ChartSessionTabManager,
    lt: Lifetime
): SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    companion object {
        val CHART_MONITORING_TAB: DataKey<LiveChartSessionTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.ChartSessionTab")
    }

    private val panel: CounterChartPanel = CounterChartPanel()

    init {
        setContent(panel)
        initActionToolbar()

        session.valueReceived.advise(lt) { valueReceived(it) }
    }

    private fun initActionToolbar() {
    }

    private fun valueReceived(value: ChartValue) {
        panel.update(value)
    }

    override fun getData(dataId: String): Any? {
        if (CHART_MONITORING_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}