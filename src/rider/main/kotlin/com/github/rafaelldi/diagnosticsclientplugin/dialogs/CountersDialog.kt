package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.counters.CounterSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.*
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class CountersDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>,
    private val persistent: Boolean
) : DialogWrapper(project) {

    private val model = CounterSettings.getInstance(project).getModel(selected)

    init {
        init()
        val action = if (persistent) "Collect" else "Monitor"
        title = "$action Counters"
        setOKButtonText(action)
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var attachToProcess: Cell<JBRadioButton>
        lateinit var launchNewProcess: Cell<JBRadioButton>
        lateinit var periodStoppingType: Cell<JBRadioButton>
        lateinit var metricsEnablingFlag: Cell<JBCheckBox>

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
                .validationOnApply {
                    if (attachToProcess.component.isSelected && it.selectedItem == null) {
                        return@validationOnApply error("Please select a process")
                    } else {
                        return@validationOnApply null
                    }
                }
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

        row("Refresh interval (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::interval)
        }.bottomGap(BottomGap.SMALL)

        buttonsGroup {
            row("Stop collection:") {
                radioButton(StoppingType.Manually.label, StoppingType.Manually)
                periodStoppingType = radioButton(StoppingType.AfterPeriod.label, StoppingType.AfterPeriod)
            }
        }.bind(model::stoppingType)
        row("Duration (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
                .enabledIf(periodStoppingType.selected)
        }.bottomGap(BottomGap.SMALL)

        row("Providers:") {
            expandableTextField()
                .align(Align.FILL)
                .validationOnInput {
                    if (isValidCounterProviderList(it.text)) {
                        return@validationOnInput null
                    } else {
                        return@validationOnInput error("Invalid providers format")
                    }
                }
                .bindText(model::providers)
        }
        collapsibleGroup("Metrics") {
            row {
                metricsEnablingFlag = checkBox("Enable metrics")
            }
            row("List of metrics:") {
                expandableTextField()
                    .align(Align.FILL)
                    .validationOnInput {
                        if (isValidMetricList(it.text)) {
                            return@validationOnInput null
                        } else {
                            return@validationOnInput error("Invalid metrics format")
                        }
                    }
                    .bindText(model::metrics)
                    .enabledIf(metricsEnablingFlag.selected)
            }
            row("Maximum number of time series") {
                intTextField()
                    .bindIntText(model::maxTimeSeries)
                    .enabledIf(metricsEnablingFlag.selected)
            }
            row("Maximum number of histograms") {
                intTextField()
                    .bindIntText(model::maxHistograms)
                    .enabledIf(metricsEnablingFlag.selected)
            }
        }
        group("File Settings") {
            buttonsGroup {
                row("File format:") {
                    for (format in CounterFileFormat.values()) {
                        radioButton(format.name, format)
                    }
                }
            }.bind(model::format)
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

    fun getModel(): CounterModel = model

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.counters"
}