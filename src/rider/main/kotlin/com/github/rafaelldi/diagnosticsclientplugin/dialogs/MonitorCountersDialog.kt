package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidCounterProviderList
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidMetricList
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MonitorCountersDialog(project: Project) : DialogWrapper(project) {
    private val model: MonitorCountersModel =
        MonitorCountersModel(
            1,
            StoppingType.AfterPeriod,
            30,
            "System.Runtime",
            "",
            1000,
            10
        )

    init {
        init()
        title = "Monitor Counters"
        setOKButtonText("Start")
    }

    override fun createCenterPanel(): JComponent = panel {
        lateinit var periodStoppingType: Cell<JBRadioButton>
        lateinit var metricsEnablingFlag: Cell<JBCheckBox>

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
                .columns(COLUMNS_MEDIUM)
                .validationOnInput {
                    if (isValidCounterProviderList(it.text)) {
                        return@validationOnInput null
                    } else {
                        return@validationOnInput error("Invalid providers format")
                    }
                }
                .bindText(model::providers)
        }
        groupRowsRange("Metrics") {
            row {
                metricsEnablingFlag = checkBox("Enable metrics")
            }
            row("List of metrics:") {
                expandableTextField()
                    .columns(COLUMNS_MEDIUM)
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
    }

    fun getModel(): MonitorCountersModel = model

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.counters"
}