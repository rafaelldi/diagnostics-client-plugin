package com.github.rafaelldi.diagnosticsclientplugin

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

class DiagnosticsClientBundle : DynamicBundle(BUNDLE) {
    companion object {
        private const val BUNDLE = "messages.DiagnosticsClientBundle"
        private val instance = DiagnosticsClientBundle()

        @Nls
        fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
            return instance.getMessage(key, *params)
        }
    }
}