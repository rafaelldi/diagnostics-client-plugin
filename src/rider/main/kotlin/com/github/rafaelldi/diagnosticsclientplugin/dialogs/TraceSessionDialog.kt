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

class TraceSessionDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
    private val export: Boolean
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
            if (export) DiagnosticsClientBundle.message("dialog.traces.title.collect")
            else DiagnosticsClientBundle.message("dialog.traces.title.watch")
        val action =
            if (export) DiagnosticsClientBundle.message("dialog.traces.button.collect")
            else DiagnosticsClientBundle.message("dialog.traces.button.watch")
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
                .bindItem(model::selectedProcess)
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

        group(DiagnosticsClientBundle.message("dialog.traces.group.providers")) {
            row(DiagnosticsClientBundle.message("dialog.traces.row.profile")) {
                profileComboBox = comboBox(TracingProfile.values().toList())
                    .bindItem(model::profile.toNullableProperty())
            }
            row(DiagnosticsClientBundle.message("dialog.traces.row.providers")) {
                providerTextField = expandableTextField()
                    .align(Align.FILL)
                    .bindText(model::providers)
            }
        }.visible(export)

        collapsibleGroup(DiagnosticsClientBundle.message("dialog.traces.group.predefined.providers")) {
            threeColumnsRow({
                httpCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.http"))
                    .bindSelected(model::http)
            }, {
                @Suppress("DialogTitleCapitalization")
                aspNetCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.asp.net.core"))
                    .bindSelected(model::aspNet)
            }, {
                @Suppress("DialogTitleCapitalization")
                efCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.ef.core"))
                    .bindSelected(model::ef)
            })
            threeColumnsRow({
                tasksCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.tasks"))
                    .bindSelected(model::tasks)
            }, {
                threadsCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.threads"))
                    .bindSelected(model::threads)
            }, {
                contentionsCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.contentions"))
                    .bindSelected(model::contentions)
            })
            twoColumnsRow({
                exceptionsCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.exceptions"))
                    .bindSelected(model::exceptions)
            }, {
                loaderCheckBox = checkBox(DiagnosticsClientBundle.message("dialog.traces.row.loader"))
                    .bindSelected(model::loader)
            })
        }.apply {
            expanded = !export
        }

        group(DiagnosticsClientBundle.message("dialog.traces.group.file.settings")) {
            row(DiagnosticsClientBundle.message("dialog.traces.row.output.filename")) {
                textField()
                    .align(Align.FILL)
                    .validationOnInput {
                        if (isValidFilename(it.text)) {
                            return@validationOnInput null
                        } else {
                            return@validationOnInput error(DiagnosticsClientBundle.message("dialog.traces.row.output.filename.error"))
                        }
                    }
                    .bindText(model::filename)
            }
            row(DiagnosticsClientBundle.message("dialog.traces.row.output.folder")) {
                textFieldWithBrowseButton(
                    DiagnosticsClientBundle.message("dialog.traces.row.output.folder.dialog.title"),
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(Align.FILL)
                    .validationOnApply {
                        if (export && it.text.isEmpty()) {
                            return@validationOnApply error(DiagnosticsClientBundle.message("dialog.traces.row.output.folder.error"))
                        } else {
                            return@validationOnApply null
                        }
                    }
                    .bindText(model::path)
            }
        }.visible(export)
    }

    fun getModel(): TraceSessionModel = model

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

        return if (export) {
            if (isProviderSelected || isProfileSelected || isPredefinedProviderSelected) null
            else ValidationInfo(DiagnosticsClientBundle.message("dialog.traces.validation.specify.provider.or.profile"))
        } else {
            if (isPredefinedProviderSelected) null
            else ValidationInfo(DiagnosticsClientBundle.message("dialog.traces.validation.select.provider"))
        }
    }

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.traces"
}