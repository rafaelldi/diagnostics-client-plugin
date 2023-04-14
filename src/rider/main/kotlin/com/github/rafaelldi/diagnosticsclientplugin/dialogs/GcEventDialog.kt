package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
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
        title =
            if (persistent) DiagnosticsClientBundle.message("dialog.gc.title.collect")
            else DiagnosticsClientBundle.message("dialog.gc.title.monitor")
        val action =
            if (persistent) DiagnosticsClientBundle.message("dialog.gc.button.collect")
            else DiagnosticsClientBundle.message("dialog.gc.button.monitor")
        setOKButtonText(action)
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()

        row(DiagnosticsClientBundle.message("dialog.gc.row.process")) {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .validationOnApply {
                    if (it.selectedItem == null) {
                        return@validationOnApply error(DiagnosticsClientBundle.message("dialog.gc.row.process.error"))
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindItemNullable(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row(DiagnosticsClientBundle.message("dialog.gc.row.stop")) {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row(DiagnosticsClientBundle.message("dialog.gc.row.duration")) {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }

        group(DiagnosticsClientBundle.message("dialog.gc.group.file.settings")) {
            row(DiagnosticsClientBundle.message("dialog.gc.row.output.filename")) {
                textField()
                    .align(Align.FILL)
                    .validationOnInput {
                        if (isValidFilename(it.text)) {
                            return@validationOnInput null
                        } else {
                            return@validationOnInput error(DiagnosticsClientBundle.message("dialog.gc.row.output.filename.error"))
                        }
                    }
                    .bindText(model::filename)
            }
            row(DiagnosticsClientBundle.message("dialog.gc.row.output.folder")) {
                textFieldWithBrowseButton(
                    DiagnosticsClientBundle.message("dialog.gc.row.output.folder.dialog.title"),
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(Align.FILL)
                    .validationOnApply {
                        if (persistent && it.text.isEmpty()) {
                            return@validationOnApply error(DiagnosticsClientBundle.message("dialog.gc.row.output.folder.error"))
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