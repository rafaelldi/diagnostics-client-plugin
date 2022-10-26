package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.traces.TraceSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MonitorTracesDialog(project: Project) : DialogWrapper(project) {
    private val model = TraceSettings.getInstance(project).getMonitorModel()

    init {
        init()
        title = "Monitor Traces"
        setOKButtonText("Start")
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
        group("Providers") {
            row {
                checkBox("Http")
                    .bindSelected(model::http)
                @Suppress("DialogTitleCapitalization")
                checkBox("ASP.NET Core")
                    .bindSelected(model::aspNet)
                @Suppress("DialogTitleCapitalization")
                checkBox("EF Core")
                    .bindSelected(model::ef)
            }.layout(RowLayout.PARENT_GRID)
            row {
                checkBox("Exceptions")
                    .bindSelected(model::exceptions)
                checkBox("Threads")
                    .bindSelected(model::threads)
                checkBox("Contentions")
                    .bindSelected(model::contentions)
            }.layout(RowLayout.PARENT_GRID)
            row {
                checkBox("Tasks")
                    .bindSelected(model::tasks)
                checkBox("Loader")
                    .bindSelected(model::loader)
            }.layout(RowLayout.PARENT_GRID)
        }
    }

    fun getModel(): MonitorTracesModel = model

    override fun getHelpId(): String = "com.github.rafaelldi.diagnosticsclientplugin.traces"
}