package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEvent
import com.github.rafaelldi.diagnosticsclientplugin.generated.GcMonitoringSession
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsClientDataKeys
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.DiagnosticsTabsManager
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.GcTableComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import java.awt.BorderLayout
import javax.swing.JPanel

class MonitorGcTab(
    private val session: GcMonitoringSession,
    private val tabsManager: DiagnosticsTabsManager,
    lt: Lifetime
) : SimpleToolWindowPanel(false), Disposable {

    private val table: GcTableComponent

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        table = GcTableComponent()
        add(JBScrollPane(table))
    }

    init {
        setContent(panel)
        initActionToolbar()

        session.gcHappened.advise(lt) { gcHappened(it) }
        session.active.advise(lt) { statusChanged(it) }
    }

    fun getSessionPid() = session.pid

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.GcSession") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.CountersSession.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = this
        toolbar = actionToolbar.component
    }

    private fun gcHappened(gcEvent: GcEvent) {
        table.add(gcEvent)
    }

    private fun statusChanged(isActive: Boolean) {
        if (isActive) {
            tabsManager.activateGcSessionTab(session.pid)
        } else {
            tabsManager.deactivateGcSessionTab(session.pid)
        }
    }

    override fun getData(dataId: String): Any? {
        if (DiagnosticsClientDataKeys.MONITOR_GC_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }

    override fun dispose() {
        session.close.fire(Unit)
    }
}