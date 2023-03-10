import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("org.jetbrains.intellij") version "1.13.2"
    id("org.jetbrains.changelog") version "2.0.0"
    id("org.jetbrains.qodana") version "0.1.13"
    id("com.jetbrains.rdgen") version "2023.1.1" // https://search.maven.org/artifact/com.jetbrains.rd/rd-gen
}

group = properties("pluginGroup")
version = properties("pluginVersion")

val rdLibDirectory: () -> File = { file("${tasks.setupDependencies.get().idea.get().classes}/lib/rd") }
extra["rdLibDirectory"] = rdLibDirectory

// Configure project's dependencies
repositories {
    maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main {
        java.srcDir("src/rider/main/kotlin")
        resources.srcDir("src/rider/main/resources")
    }
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) })
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(provider { file(".qodana").canonicalPath })
    reportPath.set(provider { file("build/reports/inspections").canonicalPath })
    saveReport.set(true)
    showReport.set(environment("QODANA_SHOW_REPORT").map { it.toBoolean() }.getOrElse(false))
}

tasks {
    val dotNetPluginId = properties("dotnetPluginId").get()
    val buildConfiguration = properties("buildConfiguration").get()

    rdgen {
        val modelDir = projectDir.resolve("protocol").resolve("src").resolve("main").resolve("kotlin")
            .resolve("model").resolve("rider")
        val pluginSourcePath = projectDir.resolve("src")
        val ktOutput = pluginSourcePath.resolve("rider").resolve("main").resolve("kotlin").resolve("com")
            .resolve("github").resolve("rafaelldi").resolve("diagnosticsclientplugin").resolve("generated")
        val csOutput = pluginSourcePath.resolve("dotnet").resolve(dotNetPluginId).resolve("Generated")

        verbose = true
        classpath({
            rdLibDirectory().resolve("rider-model.jar").canonicalPath
        })
        sources(modelDir)
        hashFolder = "$rootDir/build/rdgen/rider"
        packages = "model.rider"

        generator {
            language = "kotlin"
            transform = "asis"
            root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
            directory = ktOutput.canonicalPath
        }

        generator {
            language = "csharp"
            transform = "reversed"
            root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
            directory = csOutput.canonicalPath
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    val compileDotNet by registering {
        doLast {
            exec {
                executable("dotnet")
                args("build", "-c", buildConfiguration, "$dotNetPluginId.sln")
            }
        }
    }

    buildPlugin {
        dependsOn(compileDotNet)
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        })

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes.set(properties("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        })
    }

    prepareSandbox {
        dependsOn(compileDotNet)

        val outputFolder = file("$projectDir/src/dotnet/$dotNetPluginId/bin/$dotNetPluginId/$buildConfiguration")
        val dllFiles = listOf(
            "$outputFolder/${dotNetPluginId}.dll",
            "$outputFolder/${dotNetPluginId}.pdb",
            "$outputFolder/Microsoft.Diagnostics.Tracing.TraceEvent.dll",
            "$outputFolder/Microsoft.Diagnostics.FastSerialization.dll"
        )

        for (f in dllFiles) {
            from(f) { into("${rootProject.name}/dotnet") }
        }

        doLast {
            for (f in dllFiles) {
                val file = file(f)
                if (!file.exists()) throw RuntimeException("File \"$file\" does not exist")
            }
        }
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    runIde {
        dependsOn(compileDotNet)
        maxHeapSize = "1500m"
    }

    signPlugin {
        certificateChain.set(environment("CERTIFICATE_CHAIN"))
        privateKey.set(environment("PRIVATE_KEY"))
        password.set(environment("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(environment("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(properties("pluginVersion").map {
            listOf(
                it.split('-').getOrElse(1) { "default" }.split('.').first()
            )
        })
    }
}
