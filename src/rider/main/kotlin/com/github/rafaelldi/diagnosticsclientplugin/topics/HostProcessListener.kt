package com.github.rafaelldi.diagnosticsclientplugin.topics

import com.github.rafaelldi.diagnosticsclientplugin.model.ProcessInfo
import com.intellij.util.messages.Topic

interface HostProcessListener {
    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic.create("Host Process Listener", HostProcessListener::class.java)
    }

    fun processAdded(pid: Int, processInfo: ProcessInfo)
    fun processRemoved(pid: Int, processInfo: ProcessInfo)
}