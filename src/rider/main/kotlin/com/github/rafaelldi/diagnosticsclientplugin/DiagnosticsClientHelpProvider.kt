package com.github.rafaelldi.diagnosticsclientplugin

import com.intellij.openapi.help.WebHelpProvider

class DiagnosticsClientHelpProvider : WebHelpProvider() {
    companion object {
        const val HELP_ID_PREFIX = "com.github.rafaelldi.diagnosticsclientplugin."
        private const val HELP_DOCS = "https://rafaelldi.github.io/diagnostics-client-plugin"
        private const val MAIN = "starter-topic.html"
        private const val COUNTERS_AND_METRICS = "counters-and-metrics.html"
    }

    override fun getHelpPageUrl(helpTopicId: String): String? {
        if (!helpTopicId.startsWith(HELP_ID_PREFIX)) {
            return null
        }
        return when (helpTopicId.removePrefix(HELP_ID_PREFIX)) {
            "main" -> "${HELP_DOCS}/${MAIN}"
            "counters" -> "${HELP_DOCS}/${COUNTERS_AND_METRICS}"
            "traces" -> "${HELP_DOCS}/${MAIN}"
            else -> "${HELP_DOCS}/${MAIN}"
        }
    }
}