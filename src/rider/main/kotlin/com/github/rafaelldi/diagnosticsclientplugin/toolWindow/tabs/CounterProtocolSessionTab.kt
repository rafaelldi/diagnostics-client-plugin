package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.model.Counter
import com.github.rafaelldi.diagnosticsclientplugin.model.CounterProtocolSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.CounterSessionTabManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.CounterTable
import com.github.rafaelldi.diagnosticsclientplugin.utils.SESSION_PROCESS_ID
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.rd.util.lifetime.Lifetime

class CounterProtocolSessionTab(
    override val pid: Int,
    session: CounterProtocolSession,
    private val manager: CounterSessionTabManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), MonitoringTab, Disposable {

    private val table = CounterTable()

    private val panel = BorderLayoutPanel().apply {
        val scrollPane = ScrollPaneFactory.createScrollPane(table, true)
        addToCenter(scrollPane)
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
        if (SESSION_PROCESS_ID.`is`(dataId)) return pid
        return super.getData(dataId)
    }

    override fun dispose() {
        manager.tabClosed(pid)
    }
}