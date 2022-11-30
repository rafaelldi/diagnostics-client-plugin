package com.github.rafaelldi.diagnosticsclientplugin

import com.intellij.openapi.help.WebHelpProvider

class DiagnosticsClientHelpProvider : WebHelpProvider() {
    companion object {
        const val HELP_ID_PREFIX = "com.github.rafaelldi.diagnosticsclientplugin."
        private const val GITHUB_WIKI_URL = "https://github.com/rafaelldi/diagnostics-client-plugin/wiki"
        private const val COUNTERS_AND_METRICS = "Counters-and-Metrics"
        private const val TRACES = "Traces"
    }

    override fun getHelpPageUrl(helpTopicId: String): String? {
        if (!helpTopicId.startsWith(HELP_ID_PREFIX)) {
            return null
        }
        return when (helpTopicId.removePrefix(HELP_ID_PREFIX)) {
            "main" -> GITHUB_WIKI_URL
            "counters" -> "${GITHUB_WIKI_URL}/${COUNTERS_AND_METRICS}"
            "traces" -> "${GITHUB_WIKI_URL}/${TRACES}"
            else -> GITHUB_WIKI_URL
        }
    }
}