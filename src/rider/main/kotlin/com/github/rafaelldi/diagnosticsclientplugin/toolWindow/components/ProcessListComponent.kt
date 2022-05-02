package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.ProcessInfo
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.components.JBList
import icons.RiderIcons
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.tree.TreeSelectionModel

class ProcessListComponent : JBList<ProcessInfo>() {
    private val listModel = DefaultListModel<ProcessInfo>()

    init {
        model = listModel
        emptyText.text = "No processes to display"
        selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        cellRenderer = CellRenderer()
        ListSpeedSearch(this)
    }

    val selectedProcessId: Int?
        get() = selectedValue?.processId

    fun add(item: ProcessInfo) {
        listModel.addElement(item)
    }

    fun remove(item: ProcessInfo) {
        listModel.removeElement(item)
    }

    private class CellRenderer : ColoredListCellRenderer<ProcessInfo>() {
        override fun customizeCellRenderer(
            list: JList<out ProcessInfo>,
            value: ProcessInfo?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean
        ) {
            if (value == null) {
                return
            }

            append("${value.processId} - ${value.processName}")
            icon = RiderIcons.RunConfigurations.DotNetProject
        }
    }
}