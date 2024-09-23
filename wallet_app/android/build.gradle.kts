// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jmailen.kotlinter") version "3.15.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
