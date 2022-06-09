package com.github.rafaelldi.diagnosticsclientplugin

import com.intellij.openapi.help.WebHelpProvider

class DiagnosticsClientHelpProvider : WebHelpProvider() {
    override fun getHelpPageUrl(helpTopicId: String): String =
        "https://github.com/rafaelldi/diagnostics-client-plugin/wiki"
}