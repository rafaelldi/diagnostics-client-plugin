package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.GcEvent
import com.github.rafaelldi.diagnosticsclientplugin.model.GcEventSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.GcEventSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.GcEventTable
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.rd.util.lifetime.Lifetime

class GcEventProtocolSessionTab(
    override val pid: Int,
    session: GcEventSession,
    private val manager: GcEventSessionTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    private val table = GcEventTable()

    private val panel = BorderLayoutPanel().apply {
        val scrollPane = ScrollPaneFactory.createScrollPane(table, true)
        addToCenter(scrollPane)
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