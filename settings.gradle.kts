pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/" )
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SkyboxEngine"
