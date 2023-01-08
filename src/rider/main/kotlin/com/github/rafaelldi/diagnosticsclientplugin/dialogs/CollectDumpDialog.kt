package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.services.DumpSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class CollectDumpDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>
) : DialogWrapper(project) {
    private val model = DumpSettings.getInstance(project).getModel(selected)

    init {
        init()
        title = "Collect Dump"
        setOKButtonText("Collect")
    }

    override fun createCenterPanel(): JComponent = panel {
        val ps = processes.sortedBy { it.pid }.toList()

        row("Process:") {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .bindItemNullable(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        row("Type:") {
            comboBox(DumpType.values().toList())
                .bindItem(model::type.toNullableProperty())
        }.bottomGap(BottomGap.SMALL)

        row("Output folder:") {
            textFieldWithBrowseButton(
                "Select Path",
                project,
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
            )
                .align(Align.FILL)
                .validationOnApply {
                    if (it.text.isEmpty()) {
                        return@validationOnApply error("Please choose a folder")
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindText(model::path)
        }
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
        }.bottomGap(BottomGap.SMALL)

        row {
            checkBox("Enables diagnostic logging")
                .bindSelected(model::diag)
        }
    }

    fun getModel(): CollectDumpModel = model
}