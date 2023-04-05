package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class TraceDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
    private val persistent: Boolean
) : DialogWrapper(project) {

    private val model = TraceSettings.getInstance(project).getModel(selected)

    private lateinit var profileComboBox: Cell<ComboBox<TracingProfile>>
    private lateinit var providerTextField: Cell<ExpandableTextField>
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
        title =
            if (persistent) DiagnosticsClientBundle.message("dialog.traces.title.collect")
            else DiagnosticsClientBundle.message("dialog.traces.title.monitor")
        val action =
            if (persistent) DiagnosticsClientBundle.message("dialog.traces.button.collect")
            else DiagnosticsClientBundle.message("dialog.traces.button.monitor")
        setOKButtonText(action)
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()

        row(DiagnosticsClientBundle.message("dialog.traces.row.process")) {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .validationOnApply {
                    if (it.selectedItem == null) {
                        return@validationOnApply error(DiagnosticsClientBundle.message("dialog.traces.row.process.error"))
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindItemNullable(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row(DiagnosticsClientBundle.message("dialog.traces.row.stop")) {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row(DiagnosticsClientBundle.message("dialog.traces.row.duration")) {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }

        group("Providers") {
            row("Profile:") {
                profileComboBox = comboBox(TracingProfile.values().toList())
                    .bindItem(model::profile.toNullableProperty())
            }
            row("Providers:") {
                providerTextField = expandableTextField()
                    .align(Align.FILL)
                    .bindText(model::providers)
            }
        }.visible(persistent)

        collapsibleGroup("Predefined Providers") {
            threeColumnsRow({
                httpCheckBox = checkBox("Http")
                    .bindSelected(model::http)
            }, {
                @Suppress("DialogTitleCapitalization")
                aspNetCheckBox = checkBox("ASP.NET Core")
                    .bindSelected(model::aspNet)
            }, {
                @Suppress("DialogTitleCapitalization")
                efCheckBox = checkBox("EF Core")
                    .bindSelected(model::ef)
            })
            threeColumnsRow({
                tasksCheckBox = checkBox("Tasks")
                    .bindSelected(model::tasks)
            }, {
                threadsCheckBox = checkBox("Threads")
                    .bindSelected(model::threads)
            }, {
                contentionsCheckBox = checkBox("Contentions")
                    .bindSelected(model::contentions)
            })
            twoColumnsRow({
                exceptionsCheckBox = checkBox("Exceptions")
                    .bindSelected(model::exceptions)
            }, {
                loaderCheckBox = checkBox("Loader")
                    .bindSelected(model::loader)
            })
        }.apply {
            expanded = !persistent
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

    fun getModel(): TraceModel = model

    override fun doValidate(): ValidationInfo? {
        val isProviderSelected = providerTextField.component.text.isNullOrEmpty().not()
        val isProfileSelected = profileComboBox.component.item != TracingProfile.None
        val isPredefinedProviderSelected = httpCheckBox.component.isSelected ||
                aspNetCheckBox.component.isSelected ||
                efCheckBox.component.isSelected ||
                exceptionsCheckBox.component.isSelected ||
                threadsCheckBox.component.isSelected ||
                contentionsCheckBox.component.isSelected ||
                tasksCheckBox.component.isSelected ||
                loaderCheckBox.component.isSelected

        return if (persistent) {
            if (isProviderSelected || isProfileSelected || isPredefinedProviderSelected) null
            else ValidationInfo("Please specify a provider or select a profile")
        } else {
            if (isPredefinedProviderSelected) null
            else ValidationInfo("Please select a provider")
        }
    }

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.traces"
}