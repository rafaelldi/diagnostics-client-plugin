package com.github.rafaelldi.diagnosticsclientplugin.utils

private const val FILENAME_PATTERN = "^\\w(?:[\\w. -]*\\w)?(?:\\.\\w+)?\$"
private val filenameRegex = Regex(FILENAME_PATTERN)
private const val PROVIDERS_PATTERN =
    "^(?:[\\w-]+(?:\\[[\\w-]+(?:,[\\w-]+)*\\])?(?:,[\\w-]+(?:\\[[\\w-]+(?:,[\\w-]+)*\\])?)*)?\$"
private val providersRegex = Regex(PROVIDERS_PATTERN)

fun isValidFilename(filename: String): Boolean = filenameRegex.matches(filename)
fun isValidProviderList(providers: String): Boolean = providersRegex.matches(providers)