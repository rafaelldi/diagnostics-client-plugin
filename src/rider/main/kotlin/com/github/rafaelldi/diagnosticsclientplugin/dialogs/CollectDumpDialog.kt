package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.SystemInfo
import com.intellij.ui.dsl.builder.*
import com.jetbrains.rider.projectView.solutionDirectoryPath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.JComponent

class CollectDumpDialog(private val project: Project) : DialogWrapper(project) {
    private val model: CollectDumpModel =
        CollectDumpModel(
            DumpType.Full,
            project.solutionDirectoryPath.toString(),
            getDefaultFilename(),
            false
        )

    init {
        init()
        title = "Collect Dump"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Type:") {
            comboBox(DumpType.values().toList())
                .bindItem(model::type.toNullableProperty())
        }
        row("Output folder:") {
            textFieldWithBrowseButton(
                "Select Path",
                project,
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
            ).bindText(model::path)
        }
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
        row {
            checkBox("Enables diagnostic logging")
                .bindSelected(model::diag)
        }
    }

    fun getModel(): CollectDumpModel = model

    private fun getDefaultFilename(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val formatted = current.format(formatter)
        return if (SystemInfo.isWindows) {
            "dump_${formatted}.dmp"
        } else {
            "core_${formatted}"
        }
    }
}