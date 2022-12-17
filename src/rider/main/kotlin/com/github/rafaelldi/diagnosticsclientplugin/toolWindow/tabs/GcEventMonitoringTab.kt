package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEvent
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEventMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.GcEventTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.GcEventTableComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import java.awt.BorderLayout
import javax.swing.JPanel

class GcEventMonitoringTab(
    override val pid: Int,
    session: GcEventMonitoringSession,
    private val manager: GcEventTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    companion object {
        val GC_EVENT_MONITORING_TAB: DataKey<GcEventMonitoringTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.GcEventMonitoringTab")
    }

    private val table: GcEventTableComponent

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        table = GcEventTableComponent()
        add(JBScrollPane(table))
    }

    init {
        setContent(panel)
        initActionToolbar()

        session.gcHappened.advise(lt) { gcHappened(it) }
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.GcEventSession") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.GcEventSession.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    private fun gcHappened(gcEvent: GcEvent) {
        table.add(gcEvent)
    }

    override fun getData(dataId: String): Any? {
        if (GC_EVENT_MONITORING_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}