package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import com.jetbrains.rider.projectView.solutionDirectoryPath
import javax.swing.JComponent

class CollectTracesDialog(private val project: Project) : DialogWrapper(project) {
    private val model: CollectTracesModel =
        CollectTracesModel(
            project.solutionDirectoryPath.toString(),
            "trace.nettrace",
            StoppingType.AfterPeriod,
            30,
            TracingProfile.CpuSampling
        )

    init {
        init()
        title = "Collect Traces"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

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
        groupRowsRange("Providers") {
            row("Profile:") {
                comboBox(TracingProfile.values().toList())
                    .bindItem(model::profile.toNullableProperty())
            }
        }
        groupRowsRange("File Settings") {
            row("Output filename:") {
                textField()
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
                ).bindText(model::path)
            }
        }
    }

    fun getModel(): CollectTracesModel = model
}