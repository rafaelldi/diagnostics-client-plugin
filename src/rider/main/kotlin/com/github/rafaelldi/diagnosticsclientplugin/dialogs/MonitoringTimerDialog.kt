package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MonitoringTimerDialog(project: Project) : DialogWrapper(project) {
    private val model: MonitoringTimerModel = MonitoringTimerModel(StoppingType.Manually,30)

    init {
        init()
        title = DiagnosticsClientBundle.message("dialog.watch.title")
        setOKButtonText(DiagnosticsClientBundle.message("dialog.watch.button"))
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        buttonsGroup {
            row(DiagnosticsClientBundle.message("dialog.watch.row.stop")) {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row(DiagnosticsClientBundle.message("dialog.watch.row.duration")) {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }
    }

    fun getModel(): MonitoringTimerModel = model
}