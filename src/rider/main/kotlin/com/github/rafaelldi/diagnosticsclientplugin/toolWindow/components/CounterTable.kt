package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.Counter
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class CounterTable : JBTable() {
    companion object {
        private val COUNTER_COLUMN_TITLE = DiagnosticsClientBundle.message("counter.table.counter.column")
        private val VALUE_COLUMN_TITLE = DiagnosticsClientBundle.message("counter.table.value.column")
        private val TAGS_COLUMN_TITLE = DiagnosticsClientBundle.message("counter.table.tags.column")
    }

    private val counterRowMap: MutableMap<String, Int> = mutableMapOf()
    private val tableModel: DefaultTableModel =
        DefaultTableModel(arrayOf(COUNTER_COLUMN_TITLE, VALUE_COLUMN_TITLE, TAGS_COLUMN_TITLE), 0)

    init {
        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun add(key: String, counter: Counter) {
        val index = (counterRowMap.values.maxOrNull() ?: -1) + 1
        tableModel.addRow(arrayOf(counter.name, counter.value, counter.tags))
        counterRowMap[key] = index
    }

    fun update(key: String, counter: Counter) {
        val row = counterRowMap[key]
        if (row != null) {
            val currentValue = tableModel.getValueAt(row, 1)
            if (currentValue != counter.value) {
                tableModel.setValueAt(counter.value, row, 1)
            }
        } else {
            add(key, counter)
        }
    }

    fun remove(key: String) {
        val row = counterRowMap[key]
        if (row != null) {
            tableModel.removeRow(row)
            counterRowMap.remove(key)
        }
    }
}