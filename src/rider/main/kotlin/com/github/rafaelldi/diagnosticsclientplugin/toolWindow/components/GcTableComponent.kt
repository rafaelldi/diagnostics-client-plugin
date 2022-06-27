package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.GcEvent
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class GcTableComponent : JBTable() {
    companion object {
        private const val NUMBER_COLUMN_TITLE = "Number"
        private const val GENERATION_COLUMN_TITLE = "Generation"
    }

    private val tableModel: DefaultTableModel =
        DefaultTableModel(
            arrayOf(
                NUMBER_COLUMN_TITLE,
                GENERATION_COLUMN_TITLE
            ), 0
        )

    init {
        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun add(gcEvent: GcEvent) {
        tableModel.addRow(arrayOf(gcEvent.number, gcEvent.generation))
    }
}