rootProject.name = "diagnostics-client-plugin"

pluginManagement {
    repositories {
        maven { setUrl("https://cache-redirector.jetbrains.com/plugins.gradle.org") }
    }
    resolutionStrategy {
        eachPlugin {
            when(requested.id.name) {
                "rdgen" -> {
                    useModule("com.jetbrains.rd:rd-gen:${requested.version}")
                }
            }
        }
    }
}

include(":protocol")