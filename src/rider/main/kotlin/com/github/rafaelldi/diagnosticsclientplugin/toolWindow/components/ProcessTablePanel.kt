package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.ProcessInfo
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import javax.swing.JLabel

class ProcessTablePanel : BorderLayoutPanel() {
    private lateinit var processId: Cell<JLabel>
    private lateinit var processName: Cell<JLabel>
    private lateinit var filename: Cell<JLabel>
    private lateinit var startTime: Cell<JLabel>

    init {
        border = JBUI.Borders.empty(5, 10)

        val panel = panel {
            group("Process Details") {
                row {
                    label("Id:").bold()
                    processId = label("-")
                }
                row {
                    label("Name:").bold()
                    processName = label("-")
                }
                row {
                    label("Filename:").bold()
                    filename = label("-")
                }
                row {
                    label("Start time:").bold()
                    startTime = label("-")
                }
            }
        }

        add(panel)
    }

    fun update(processInfo: ProcessInfo) {
        processId.component.text = processInfo.processId.toString()
        processName.component.text = processInfo.processName
        filename.component.text = processInfo.filename
        startTime.component.text = processInfo.startTime
    }

    fun clear() {
        processId.component.text = "-"
        processName.component.text = "-"
        filename.component.text = "-"
        startTime.component.text = "-"
    }
}