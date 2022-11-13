package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.ProcessList
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.ProcessListComponent
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.ProcessTablePanel
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.AddRemove
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class ProcessExplorerTab(private val processList: ProcessList, lt: Lifetime) :
    SimpleToolWindowPanel(false), ListSelectionListener {

    companion object {
        const val SPLITTER_PROPORTION = 0.3F
        val PROCESS_EXPLORE_TAB: DataKey<ProcessExplorerTab> =
            DataKey.create("DiagnosticsClient.ToolWindow.ProcessExplorerTab")
    }

    var selectedProcessId: Int? = null
        private set

    private val processListComponent: ProcessListComponent = ProcessListComponent()
    private val processPanelComponent: ProcessTablePanel = ProcessTablePanel()

    init {
        val splitter = OnePixelSplitter(false, SPLITTER_PROPORTION).apply {
            firstComponent = JBScrollPane(processListComponent)
            secondComponent = JBScrollPane(processPanelComponent)
        }

        setContent(splitter)
        initActionToolbar()

        lt.bracketIfAlive(
            { processListComponent.addListSelectionListener(this) },
            { processListComponent.removeListSelectionListener(this) }
        )

        processList.items.adviseAddRemove(lt) { action, _, processInfo ->
            when (action) {
                AddRemove.Add -> processListComponent.add(processInfo)
                AddRemove.Remove -> processListComponent.remove(processInfo)
            }
        }
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("DiagnosticsClient.ToolWindow.Explorer") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar(
            "DiagnosticsClient.ToolWindow.Explorer.ActionToolbar",
            actionGroup,
            true
        )
        actionToolbar.targetComponent = processListComponent
        toolbar = actionToolbar.component
    }

    override fun valueChanged(e: ListSelectionEvent?) {
        if (e == null) {
            return
        }

        val selectedPid = processListComponent.selectedProcessId
        selectedProcessId = selectedPid

        if (selectedPid != null) {
            val process = processList.items.firstOrNull { it.processId == selectedPid } ?: return
            processPanelComponent.update(process)
        } else {
            processPanelComponent.clear()
        }
    }

    override fun getData(dataId: String): Any? {
        if (PROCESS_EXPLORE_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }
}