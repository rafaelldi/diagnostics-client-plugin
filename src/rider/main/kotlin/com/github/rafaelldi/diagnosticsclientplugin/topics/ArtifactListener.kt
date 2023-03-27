package com.github.rafaelldi.diagnosticsclientplugin.topics

import com.intellij.util.messages.Topic
import com.intellij.util.messages.Topic.ProjectLevel

interface ArtifactListener {
    companion object {
        @ProjectLevel
        val TOPIC = Topic.create("Artifact Listener", ArtifactListener::class.java)
    }

    fun artifactCreated(path: String)
}