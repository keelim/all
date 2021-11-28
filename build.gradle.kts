plugins {
    id("com.android.application") version("7.2.0-alpha05") apply false
    id("com.android.library") version("7.2.0-alpha05") apply false
    id("org.jetbrains.kotlin.android") version("1.5.31") apply false
}

buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.4")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.39.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.0")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
