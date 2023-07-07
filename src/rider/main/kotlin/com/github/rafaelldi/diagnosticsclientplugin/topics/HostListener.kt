package com.github.rafaelldi.diagnosticsclientplugin.topics

import com.intellij.util.messages.Topic

interface HostListener {
    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic.create("Host Listener", HostListener::class.java)
    }

    fun hostDisconnected()
}