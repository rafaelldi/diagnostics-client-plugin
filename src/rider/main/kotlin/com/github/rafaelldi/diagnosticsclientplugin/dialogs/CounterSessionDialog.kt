package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidCounterProviderList
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidMetricList
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class CounterSessionDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
    private val export: Boolean
) : DialogWrapper(project) {

    private val model = CounterSettings.getInstance(project).getModel(selected)

    init {
        init()
        title =
            if (export) DiagnosticsClientBundle.message("dialog.counters.title.collect")
            else DiagnosticsClientBundle.message("dialog.counters.title.watch")
        val action =
            if (export) DiagnosticsClientBundle.message("dialog.counters.button.collect")
            else DiagnosticsClientBundle.message("dialog.counters.button.watch")
        setOKButtonText(action)
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>

        val ps = processes.sortedBy { it.pid }.toList()

        row(DiagnosticsClientBundle.message("dialog.counters.row.process")) {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .validationOnApply {
                    if (it.selectedItem == null) {
                        return@validationOnApply error(DiagnosticsClientBundle.message("dialog.counters.row.process.error"))
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindItem(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row(DiagnosticsClientBundle.message("dialog.counters.row.stop")) {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row(DiagnosticsClientBundle.message("dialog.counters.row.duration")) {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }.bottomGap(BottomGap.SMALL)

        row(DiagnosticsClientBundle.message("dialog.counters.row.counters")) {
            expandableTextField()
                .align(Align.FILL)
                .validationOnInput {
                    if (isValidCounterProviderList(it.text)) {
                        return@validationOnInput null
                    } else {
                        return@validationOnInput error(DiagnosticsClientBundle.message("dialog.counters.row.counters.error"))
                    }
                }
                .bindText(model::providers)
        }

        row(DiagnosticsClientBundle.message("dialog.counters.row.list.of.metrics")) {
            expandableTextField()
                .align(Align.FILL)
                .validationOnInput {
                    if (isValidMetricList(it.text)) {
                        return@validationOnInput null
                    } else {
                        return@validationOnInput error(DiagnosticsClientBundle.message("dialog.counters.row.list.of.metrics.error"))
                    }
                }
                .bindText(model::metrics)
        }

        val advancedGroupExpanded = model.interval != 1 || model.maxTimeSeries != 1000 || model.maxHistograms != 10
        collapsibleGroup(DiagnosticsClientBundle.message("dialog.counters.group.advanced.settings")) {
            row(DiagnosticsClientBundle.message("dialog.counters.row.refresh.interval")) {
                spinner(1..3600, 1)
                    .bindIntValue(model::interval)
            }.bottomGap(BottomGap.SMALL)
            row(DiagnosticsClientBundle.message("dialog.counters.row.number.of.time.series")) {
                intTextField()
                    .bindIntText(model::maxTimeSeries)
            }
            row(DiagnosticsClientBundle.message("dialog.counters.row.number.of.histograms")) {
                intTextField()
                    .bindIntText(model::maxHistograms)
            }
        }.expanded = advancedGroupExpanded

        group(DiagnosticsClientBundle.message("dialog.counters.group.file.settings")) {
            buttonsGroup {
                row(DiagnosticsClientBundle.message("dialog.counters.row.file.format")) {
                    for (format in CounterFileFormat.entries) {
                        radioButton(format.name, format)
                    }
                }
            }.bind(model::format)
            row(DiagnosticsClientBundle.message("dialog.counters.row.output.filename")) {
                textField()
                    .align(Align.FILL)
                    .validationOnInput {
                        if (isValidFilename(it.text)) {
                            return@validationOnInput null
                        } else {
                            return@validationOnInput error(DiagnosticsClientBundle.message("dialog.counters.row.output.filename.error"))
                        }
                    }
                    .bindText(model::filename)
            }
            row(DiagnosticsClientBundle.message("dialog.counters.row.output.folder")) {
                textFieldWithBrowseButton(
                    DiagnosticsClientBundle.message("dialog.counters.row.output.folder.dialog.title"),
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(Align.FILL)
                    .validationOnApply {
                        if (export && it.text.isEmpty()) {
                            return@validationOnApply error(DiagnosticsClientBundle.message("dialog.counters.row.output.folder.error"))
                        } else {
                            return@validationOnApply null
                        }
                    }
                    .bindText(model::path)
            }
        }.visible(export)
    }

    fun getModel(): CounterSessionModel = model

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.counters"
}