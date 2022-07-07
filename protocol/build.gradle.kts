plugins {
    kotlin("jvm") version "1.7.10"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("", "rider-model")
    implementation("", "rd-gen")
}

val rdLibDirectory: () -> File by rootProject.extra

repositories {
    mavenCentral()
    flatDir {
        dir(rdLibDirectory())
    }
}
