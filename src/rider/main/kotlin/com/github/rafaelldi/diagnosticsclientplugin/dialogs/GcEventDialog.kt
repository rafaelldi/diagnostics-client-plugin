package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.gc.GcEventSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class GcEventDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
    private val persistent: Boolean
) : DialogWrapper(project) {

    private val model = GcEventSettings.getInstance(project).getModel(selected)

    init {
        init()
        val action = if (persistent) "Collect" else "Monitor"
        title = "$action GC Events"
        setOKButtonText(action)
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
        }

        group("File Settings") {
            row("Output filename:") {
                textField()
                    .align(Align.FILL)
                    .validationOnInput {
                        if (isValidFilename(it.text)) {
                            return@validationOnInput null
                        } else {
                            return@validationOnInput error("Invalid filename")
                        }
                    }
                    .bindText(model::filename)
            }
            row("Output folder:") {
                textFieldWithBrowseButton(
                    "Select Path",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(Align.FILL)
                    .validationOnApply {
                        if (persistent && it.text.isEmpty()) {
                            return@validationOnApply error("Please choose a folder")
                        } else {
                            return@validationOnApply null
                        }
                    }
                    .bindText(model::path)
            }
        }.visible(persistent)
    }

    fun getModel(): GcEventModel = model
}