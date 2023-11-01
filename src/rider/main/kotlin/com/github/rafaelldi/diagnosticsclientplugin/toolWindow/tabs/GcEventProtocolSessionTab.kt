package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.GcEvent
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.GcEventSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.GcEventTable
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import java.awt.BorderLayout
import javax.swing.JPanel

class GcEventProtocolSessionTab(
    override val pid: Int,
    session: GcEventProtocolSession,
    private val manager: GcEventSessionTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    private val table: GcEventTable

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        table = GcEventTable()
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
        if (SESSION_PROCESS_ID.`is`(dataId)) return pid
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}