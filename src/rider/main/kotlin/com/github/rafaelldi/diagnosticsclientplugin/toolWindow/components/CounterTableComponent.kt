package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.Counter
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class CounterTableComponent : JBTable() {
    companion object {
        private const val COUNTER_COLUMN_TITLE = "Counter"
        private const val TAGS_COLUMN_TITLE = "Tags"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    private val counterRowMap: MutableMap<String, Int> = mutableMapOf()
    private val tableModel: DefaultTableModel =
        DefaultTableModel(arrayOf(COUNTER_COLUMN_TITLE, TAGS_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

    init {
        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun add(key: String, counter: Counter) {
        val index = (counterRowMap.values.maxOrNull() ?: -1) + 1
        tableModel.addRow(arrayOf(counter.name, counter.tags, counter.value))
        counterRowMap[key] = index
    }

    fun update(key: String, counter: Counter) {
        val row = counterRowMap[key]
        if (row != null) {
            val currentValue = tableModel.getValueAt(row, 2)
            if (currentValue != counter.value) {
                tableModel.setValueAt(counter.value, row, 2)
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