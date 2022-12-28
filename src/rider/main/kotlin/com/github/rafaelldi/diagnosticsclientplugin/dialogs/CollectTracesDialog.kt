package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class CollectTracesDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>
) : DialogWrapper(project) {
    private val model = TraceSettings.getInstance(project).getCollectModel(selected)

    init {
        init()
        title = "Collect Traces"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>
        lateinit var profileComboBox: Cell<ComboBox<TracingProfile>>
        lateinit var providerTextField: Cell<ExpandableTextField>

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
            row("Profile:") {
                profileComboBox = comboBox(TracingProfile.values().toList())
                    .validationOnApply {
                        if (it.item == TracingProfile.None && providerTextField.component.text.isNullOrEmpty()) {
                            return@validationOnApply error("Please select a profile or fill in the providers field")
                        } else {
                            return@validationOnApply null
                        }
                    }
                    .bindItem(model::profile.toNullableProperty())
            }
            row("Providers:") {
                providerTextField = expandableTextField()
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
        collapsibleGroup("Predefined Providers") {
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
        group("File Settings") {
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