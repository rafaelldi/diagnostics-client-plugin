package com.github.rafaelldi.diagnosticsclientplugin

import com.intellij.openapi.help.WebHelpProvider

class DiagnosticsClientHelpProvider : WebHelpProvider() {
    companion object {
        const val HELP_ID_PREFIX = "com.github.rafaelldi.diagnosticsclientplugin."
        private const val GITHUB_WIKI_URL = "https://github.com/rafaelldi/diagnostics-client-plugin/wiki"
    }

    override fun getHelpPageUrl(helpTopicId: String): String? {
        if (!helpTopicId.startsWith(HELP_ID_PREFIX)) {
            return null
        }
        val id = helpTopicId.removePrefix(HELP_ID_PREFIX)
        return if (id == "main")
            GITHUB_WIKI_URL
        else
            "${GITHUB_WIKI_URL}#${id}"
    }
}