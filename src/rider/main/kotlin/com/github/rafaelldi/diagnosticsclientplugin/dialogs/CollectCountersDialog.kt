package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidProviderList
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import com.jetbrains.rider.projectView.solutionDirectoryPath
import javax.swing.JComponent

class CollectCountersDialog(private val project: Project) : DialogWrapper(project) {
    private val model: CollectCountersModel =
        CollectCountersModel(
            project.solutionDirectoryPath.toString(),
            "counters",
            CountersFileFormat.Csv,
            1,
            StoppingType.AfterPeriod,
            30,
            ""
        )

    init {
        init()
        title = "Collect Counters"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        row("Refresh interval (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::interval)
        }
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
        row("Providers:") {
            expandableTextField()
                .validationOnInput {
                    if (isValidProviderList(it.text)) {
                        return@validationOnInput null
                    } else {
                        return@validationOnInput error("Invalid providers format")
                    }
                }
                .comment("Leave empty for default")
                .bindText(model::providers)
        }
        groupRowsRange("File Settings") {
            buttonsGroup {
                row("File format:") {
                    for (format in CountersFileFormat.values()) {
                        radioButton(format.name, format)
                    }
                }
            }.bind(model::format)
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

    fun getModel(): CollectCountersModel = model
}