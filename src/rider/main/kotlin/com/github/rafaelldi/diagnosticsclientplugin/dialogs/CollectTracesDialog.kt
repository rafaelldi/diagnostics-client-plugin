package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.TracesSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class CollectTracesDialog(private val project: Project) : DialogWrapper(project) {
    private val model = TracesSettings.getInstance(project).getModel()

    init {
        init()
        title = "Collect Traces"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>
        lateinit var profileComboBox: Cell<ComboBox<TracingProfile>>
        lateinit var providersTextField: Cell<ExpandableTextField>

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
                profileComboBox = comboBox(TracingProfile.values().toList())
                    .validationOnApply {
                        if (it.item == TracingProfile.None && providersTextField.component.text.isNullOrEmpty()) {
                            return@validationOnApply error("Please select a profile or fill in the providers field")
                        } else {
                            return@validationOnApply null
                        }
                    }
                    .bindItem(model::profile.toNullableProperty())
            }
            row("Providers:") {
                providersTextField = expandableTextField()
                    .columns(COLUMNS_MEDIUM)
                    .validationOnApply {
                        if (it.text.isNullOrEmpty() && profileComboBox.component.item == TracingProfile.None) {
                            return@validationOnApply error("Please select a profile or fill in the providers field")
                        } else {
                            return@validationOnApply null
                        }
                    }
                    .bindText(model::providers)
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

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.traces"
}