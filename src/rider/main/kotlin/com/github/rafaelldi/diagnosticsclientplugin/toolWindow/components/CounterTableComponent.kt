package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.Counter
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class CounterTableComponent : JBTable() {
    companion object {
        private const val COUNTER_COLUMN_TITLE = "Counter"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    private val counterRowMap: MutableMap<String, Int> = mutableMapOf()
    private val tableModel: DefaultTableModel =
        DefaultTableModel(arrayOf(COUNTER_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

    init {
        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun add(counterName: String, counter: Counter) {
        val index = (counterRowMap.values.maxOrNull() ?: -1) + 1
        tableModel.addRow(arrayOf(counter.name, counter.value))
        counterRowMap[counterName] = index
    }

    fun update(counterName: String, counter: Counter) {
        val row = counterRowMap[counterName]
        if (row != null) {
            val currentValue = tableModel.getValueAt(row, 1)
            if (currentValue != counter.value) {
                tableModel.setValueAt(counter.value, row, 1)
            }
        } else {
            add(counterName, counter)
        }
    }

    fun remove(counterName: String) {
        val row = counterRowMap[counterName]
        if (row != null) {
            tableModel.removeRow(row)
            counterRowMap.remove(counterName)
        }
    }
}