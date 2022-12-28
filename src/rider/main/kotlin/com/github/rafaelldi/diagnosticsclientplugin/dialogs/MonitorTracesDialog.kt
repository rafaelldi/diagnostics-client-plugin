package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MonitorTracesDialog(
    project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>
) : DialogWrapper(project) {
    private val model = TraceSettings.getInstance(project).getMonitorModel(selected)
    private lateinit var httpCheckBox: Cell<JBCheckBox>
    private lateinit var aspNetCheckBox: Cell<JBCheckBox>
    private lateinit var efCheckBox: Cell<JBCheckBox>
    private lateinit var exceptionsCheckBox: Cell<JBCheckBox>
    private lateinit var threadsCheckBox: Cell<JBCheckBox>
    private lateinit var contentionsCheckBox: Cell<JBCheckBox>
    private lateinit var tasksCheckBox: Cell<JBCheckBox>
    private lateinit var loaderCheckBox: Cell<JBCheckBox>

    init {
        init()
        title = "Monitor Traces"
        setOKButtonText("Start")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()
        row {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .bindItemNullable(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row("Stop collection:") {
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
            }
        }.bind(model::stoppingType)
        row("Duration (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }
        group("Providers") {
            threeColumnsRow({
                checkBox("Http")
                    .bindSelected(model::http)
            }, {
                @Suppress("DialogTitleCapitalization")
                checkBox("ASP.NET Core")
                    .bindSelected(model::aspNet)
            }, {
                @Suppress("DialogTitleCapitalization")
                checkBox("EF Core")
                    .bindSelected(model::ef)
            })
            threeColumnsRow({
                checkBox("Tasks")
                    .bindSelected(model::tasks)
            }, {
                checkBox("Threads")
                    .bindSelected(model::threads)
            }, {
                checkBox("Contentions")
                    .bindSelected(model::contentions)
            })
            twoColumnsRow({
                checkBox("Exceptions")
                    .bindSelected(model::exceptions)
            }, {
                checkBox("Loader")
                    .bindSelected(model::loader)
            })
        }
    }

    fun getModel(): MonitorTracesModel = model

    override fun doValidate(): ValidationInfo? {
        return if (
            httpCheckBox.component.isSelected ||
            aspNetCheckBox.component.isSelected ||
            efCheckBox.component.isSelected ||
            exceptionsCheckBox.component.isSelected ||
            threadsCheckBox.component.isSelected ||
            contentionsCheckBox.component.isSelected ||
            tasksCheckBox.component.isSelected ||
            loaderCheckBox.component.isSelected
        ) {
            null
        } else {
            ValidationInfo("No provider selected")
        }
    }

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.traces"
}