package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.services.chart.ChartSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class ChartSessionDialog(
    project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
) : DialogWrapper(project) {

    private val model = ChartSettings.getInstance().getModel(selected)

    init {
        init()
        title = DiagnosticsClientBundle.message("dialog.chart.title")
        setOKButtonText(DiagnosticsClientBundle.message("dialog.chart.button"))
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()

        row(DiagnosticsClientBundle.message("dialog.chart.row.process")) {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .validationOnApply {
                    if (it.selectedItem == null) {
                        return@validationOnApply error(DiagnosticsClientBundle.message("dialog.chart.row.process.error"))
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindItem(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row(DiagnosticsClientBundle.message("dialog.chart.row.stop")) {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row(DiagnosticsClientBundle.message("dialog.chart.row.duration")) {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }.bottomGap(BottomGap.SMALL)
    }

    fun getModel(): ChartSessionModel = model
}