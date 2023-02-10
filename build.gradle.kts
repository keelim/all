buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.3")
        classpath("com.google.firebase:perf-plugin:1.4.2")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.qodana) apply false
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
