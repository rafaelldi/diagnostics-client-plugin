package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rider.projectView.solutionDirectoryPath
import javax.swing.JComponent

class CollectTracesDialog(private val project: Project) : DialogWrapper(project) {
    private val model: CollectTracesModel =
        CollectTracesModel(
            project.solutionDirectoryPath.toString(),
            "trace.nettrace"
        )

    init {
        init()
        title = "Collect Traces"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
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

    fun getModel(): CollectTracesModel = model
}