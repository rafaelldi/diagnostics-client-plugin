package com.github.rafaelldi.diagnosticsclientplugin.dialogs

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.services.MemoryDumpSettings
import com.github.rafaelldi.diagnosticsclientplugin.utils.DotNetProcess
import com.github.rafaelldi.diagnosticsclientplugin.utils.isValidFilename
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MemoryDumpDialog(
    private val project: Project,
    selected: DotNetProcess,
    private val processes: List<DotNetProcess>
) : DialogWrapper(project) {
    private val model = MemoryDumpSettings.getInstance(project).getModel(selected)

    init {
        init()
        title = DiagnosticsClientBundle.message("dialog.collect.dump.title")
        setOKButtonText(DiagnosticsClientBundle.message("dialog.collect.dump.button"))
    }

    override fun createCenterPanel(): JComponent = panel {
        val ps = processes.sortedBy { it.pid }.toList()

        row(DiagnosticsClientBundle.message("dialog.collect.dump.row.process")) {
            comboBox(ps, SimpleListCellRenderer.create("") { "${it.pid} - ${it.name}" })
                .align(Align.FILL)
                .bindItem(model::selectedProcess)
        }.bottomGap(BottomGap.SMALL)

        row(DiagnosticsClientBundle.message("dialog.collect.dump.row.type")) {
            comboBox(DumpType.values().toList(), SimpleListCellRenderer.create("") { it.label })
                .bindItem(model::type.toNullableProperty())
        }.bottomGap(BottomGap.SMALL)

        row(DiagnosticsClientBundle.message("dialog.collect.dump.row.output.folder")) {
            textFieldWithBrowseButton(
                DiagnosticsClientBundle.message("dialog.collect.dump.row.output.folder.dialog.title"),
                project,
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
            )
                .align(Align.FILL)
                .validationOnApply {
                    if (it.text.isEmpty()) {
                        return@validationOnApply error(DiagnosticsClientBundle.message("dialog.collect.dump.row.output.folder.error"))
                    } else {
                        return@validationOnApply null
                    }
                }
                .bindText(model::path)
        }
        row(DiagnosticsClientBundle.message("dialog.collect.dump.row.output.filename")) {
            textField()
                .align(Align.FILL)
                .validationOnInput {
                    if (isValidFilename(it.text)) {
                        return@validationOnInput null
                    } else {
                        return@validationOnInput error(DiagnosticsClientBundle.message("dialog.collect.dump.row.output.filename.error"))
                    }
                }
                .bindText(model::filename)
        }.bottomGap(BottomGap.SMALL)

        row {
            checkBox(DiagnosticsClientBundle.message("dialog.collect.dump.row.diagnostic.logging"))
                .bindSelected(model::diag)
        }
    }

    fun getModel(): MemoryDumpModel = model
}