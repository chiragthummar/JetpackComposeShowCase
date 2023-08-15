// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    dependencies {
        // Add this line
        classpath(libs.kotlin.gradle.plugin)
    }
    repositories {
        mavenCentral()
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.library) apply false
    alias(libs.plugins.ksp) apply false
}
true


