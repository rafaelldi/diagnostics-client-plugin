package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.createExecutableDescription
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MonitorGcEventsDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>
) : DialogWrapper(project) {
    private val model = GcEventSettings.getInstance(project).getMonitorModel(selected)

    init {
        init()
        title = "Monitor GC Events"
        setOKButtonText("Start")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var attachToProcess: Cell<JBRadioButton>
        lateinit var launchNewProcess: Cell<JBRadioButton>
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()

        buttonsGroup {
            row {
                attachToProcess = radioButton(SourceProcessType.Attach.label, SourceProcessType.Attach)
                launchNewProcess = radioButton(SourceProcessType.Launch.label, SourceProcessType.Launch)
            }
        }.bind(model::sourceProcessType)

        row("Process:") {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .bindItemNullable(model::selectedProcess)
        }.visibleIf(attachToProcess.selected)
            .bottomGap(BottomGap.SMALL)

        row("Executable path:") {
            textFieldWithBrowseButton(
                "Select Path",
                project,
                createExecutableDescription()
            )
                .align(Align.FILL)
                .validationOnApply {
                    if (launchNewProcess.component.isSelected && it.text.isEmpty()) {
                        return@validationOnApply error("Please select an executable")
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindText(model::executablePath)
        }.visibleIf(launchNewProcess.selected)
        row("Arguments:") {
            expandableTextField()
                .align(Align.FILL)
                .bindText(model::executableArgs)
        }.visibleIf(launchNewProcess.selected)
            .bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row("Stop monitoring:") {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row("Duration (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }
    }

    fun getModel(): MonitorGcEventsModel = model
}