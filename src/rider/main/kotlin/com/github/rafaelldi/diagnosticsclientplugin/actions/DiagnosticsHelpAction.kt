package com.github.rafaelldi.diagnosticsclientplugin.actions

import com.intellij.ide.actions.ContextHelpAction
import com.intellij.openapi.actionSystem.DataContext

class DiagnosticsClientHelpAction : ContextHelpAction() {
    override fun getHelpId(dataContext: DataContext?): String = "com.github.rafaelldi.diagnosticsclientplugin.main"
}