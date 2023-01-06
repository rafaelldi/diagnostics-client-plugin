package com.github.rafaelldi.diagnosticsclientplugin.utils

import com.intellij.openapi.fileChooser.FileChooserDescriptor

fun createExecutableDescription(): FileChooserDescriptor {
    return FileChooserDescriptor(true, false, false, false, false, false)
        .withFileFilter {
            it.extension != null &&
                    (it.extension.equals("exe", ignoreCase = true) || it.extension.equals("dll", ignoreCase = true))
        }
}