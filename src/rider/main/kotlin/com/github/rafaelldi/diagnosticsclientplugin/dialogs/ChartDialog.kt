package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class ChartDialog(
    project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
) : DialogWrapper(project) {

    private val model = ChartSettings.getInstance().getModel(selected)

    init {
        init()
        title = "Monitor Chart"
        setOKButtonText("Monitor")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()

        row("Process:") {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .validationOnApply {
                    if (it.selectedItem == null) {
                        return@validationOnApply error("Please select a process")
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindItemNullable(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row("Stop:") {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row("Duration (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }.bottomGap(BottomGap.SMALL)
    }

    fun getModel(): ChartModel = model
}