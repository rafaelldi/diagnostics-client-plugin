package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.ProcessInfo
import com.intellij.ide.CopyProvider
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.SortedListModel
import com.intellij.ui.components.JBList
import icons.RiderIcons
import java.awt.datatransfer.StringSelection
import javax.swing.JList
import javax.swing.ListSelectionModel

class ProcessListComponent : JBList<Pair<Int, ProcessInfo>>(), CopyProvider {
    private val listModel = SortedListModel<Pair<Int, ProcessInfo>>(compareBy { it.first })

    init {
        model = listModel
        emptyText.text = "Loading..."
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        cellRenderer = CellRenderer()
        ListSpeedSearch(this) { it.second.processName }
    }

    val selectedProcess: Pair<Int, String>?
        get() = selectedValue?.let { it.first to it.second.processName }

    val processes: List<Pair<Int, String>>
        get() = listModel.items.map { it.first to it.second.processName }.toList()

    fun add(pid: Int, item: ProcessInfo) {
        listModel.add(pid to item)
    }

    fun remove(pid: Int, item: ProcessInfo) {
        listModel.remove(pid to item)
    }

    private class CellRenderer : ColoredListCellRenderer<Pair<Int, ProcessInfo>>() {
        override fun customizeCellRenderer(
            list: JList<out Pair<Int, ProcessInfo>>,
            value: Pair<Int, ProcessInfo>?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean
        ) {
            if (value == null) {
                return
            }

            append("${value.first} - ${value.second.processName}")
            icon = RiderIcons.RunConfigurations.DotNetProject
        }
    }

    override fun performCopy(dataContext: DataContext) {
        if (!isSelectionEmpty) {
            CopyPasteManager.getInstance().setContents(StringSelection(getTextForCopy()))
        }
    }

    private fun getTextForCopy() = "${selectedValue.first} - ${selectedValue.second.processName}"

    override fun isCopyEnabled(dataContext: DataContext) = !isSelectionEmpty

    override fun isCopyVisible(dataContext: DataContext) = !isSelectionEmpty

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}