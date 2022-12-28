package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.tabs

import com.github.rafaelldi.diagnosticsclientplugin.generated.ProcessList
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.ProcessListComponent
import com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components.ProcessTablePanel
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
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

    private val processListComponent: ProcessListComponent = ProcessListComponent()
    private val processPanelComponent: ProcessTablePanel = ProcessTablePanel()

    var selectedProcess: DotNetProcess? = null
        private set

    val processes: List<DotNetProcess>
        get() = processListComponent.processes.map { DotNetProcess(it.first, it.second) }

    init {
        val listPanel = JBScrollPane(processListComponent)
        listPanel.border = JBUI.Borders.emptyTop(7)
        val processPanel = JBScrollPane(processPanelComponent)
        processPanel.border = JBUI.Borders.empty()

        val splitter = OnePixelSplitter(false, SPLITTER_PROPORTION).apply {
            firstComponent = listPanel
            secondComponent = processPanel
        }

        setContent(splitter)
        initActionToolbar()

        lt.bracketIfAlive(
            { processListComponent.addListSelectionListener(this) },
            { processListComponent.removeListSelectionListener(this) }
        )

        processList.items.adviseAddRemove(lt) { action, pid, processInfo ->
            when (action) {
                AddRemove.Add -> processListComponent.add(pid, processInfo)
                AddRemove.Remove -> processListComponent.remove(pid, processInfo)
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

        val selected = processListComponent.selectedProcess
        if (selected != null) {
            selectedProcess = DotNetProcess(selected.first, selected.second)
            val process = processList.items[selected.first] ?: return
            processPanelComponent.update(selected.first, process)
        } else {
            selectedProcess = null
            processPanelComponent.clear()
        }
    }

    override fun getData(dataId: String): Any? {
        if (PROCESS_EXPLORE_TAB.`is`(dataId)) return this
        return super.getData(dataId)
    }
}