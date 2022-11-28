plugins {
    kotlin("jvm") version "1.7.22"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("", "rider-model")
    implementation("", "rd-gen")
}

val rdLibDirectory: () -> File by rootProject.extra

repositories {
    maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
    flatDir {
        dir(rdLibDirectory())
    }
}
