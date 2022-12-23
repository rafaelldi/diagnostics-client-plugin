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
    private lateinit var commandLine: Cell<JLabel>
    private lateinit var operatingSystem: Cell<JLabel>
    private lateinit var processArchitecture: Cell<JLabel>

    init {
        border = JBUI.Borders.empty(5, 10)

        val panel = panel {
            group("Process Details") {
                row("Id:") {
                    processId = label("-")
                }
                row("Name:") {
                    processName = label("-")
                }
                row("Filename:") {
                    filename = label("-")
                }
                row("Start time:") {
                    startTime = label("-")
                }
                row("Command line:") {
                    commandLine = label("-")
                }
                row("Operating system:") {
                    operatingSystem = label("-")
                }
                row("Process architecture:") {
                    processArchitecture = label("-")
                }
            }
        }

        add(panel)
    }

    fun update(pid: Int, processInfo: ProcessInfo) {
        processId.component.text = pid.toString()
        processName.component.text = processInfo.processName
        filename.component.text = processInfo.filename
        startTime.component.text = processInfo.startTime
        commandLine.component.text = processInfo.commandLine
        operatingSystem.component.text = processInfo.operatingSystem
        processArchitecture.component.text = processInfo.processArchitecture
    }

    fun clear() {
        processId.component.text = "-"
        processName.component.text = "-"
        filename.component.text = "-"
        startTime.component.text = "-"
        commandLine.component.text = "-"
        operatingSystem.component.text = "-"
        processArchitecture.component.text = "-"
    }
}