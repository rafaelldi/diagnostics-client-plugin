package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MonitoringTimerDialog(project: Project) : DialogWrapper(project) {
    private val model: MonitoringTimerModel = MonitoringTimerModel(StoppingType.AfterPeriod,30)

    init {
        init()
        title = "Monitor"
        setOKButtonText("Monitor")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        buttonsGroup {
            row("Stop monitoring:") {
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
            }
        }.bind(model::stoppingType)
        row("Duration (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }
    }

    fun getModel(): MonitoringTimerModel = model
}