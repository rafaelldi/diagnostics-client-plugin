package com.github.rafaelldi.diagnosticsclientplugin.utils

private const val FILENAME_PATTERN = "^\\w(?:[\\w. -]*\\w)?(?:\\.\\w+)?\$"
private val filenameRegex = Regex(FILENAME_PATTERN)
fun isValidFilename(filename: String): Boolean = filenameRegex.matches(filename)

//Valid providers:
//System.Runtime
//System.Runtime[cpu-usage,working-set]
//System.Net.Http[http11-connections-current-total,http20-connections-current-total]
//System.Runtime[cpu-usage,working-set],Microsoft.AspNetCore.Hosting
private const val COUNTER_PROVIDERS_PATTERN =
    "^(?:[\\w\\-.]+(?:\\[[\\w-]+(?:,[\\w-]+)*\\])?(?:,[\\w\\-.]+(?:\\[[\\w-]+(?:,[\\w-]+)*\\])?)*)?\$"
private val counterProvidersRegex = Regex(COUNTER_PROVIDERS_PATTERN)
fun isValidCounterProviderList(providers: String): Boolean = counterProvidersRegex.matches(providers)