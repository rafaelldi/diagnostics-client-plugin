package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class CountersTimerDialog(project: Project) : DialogWrapper(project) {
    private val model: CountersTimerModel = CountersTimerModel(30)

    init {
        init()
        title = "Monitor Counters"
        setOKButtonText("Start")
    }

    override fun createCenterPanel(): JComponent? = panel {
        row("Duration (sec.):") {
            spinner(1..3600, 1)
                .bindIntValue(model::duration)
        }
    }

    fun getModel(): CountersTimerModel = model
}