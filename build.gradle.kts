buildscript {
  repositories {
    google()  // Google's Maven repository
    mavenCentral()
    maven("https://jitpack.io")
  }
  dependencies {
    classpath("com.google.gms:google-services:4.3.14")
    classpath("com.google.android.gms:oss-licenses-plugin:0.10.5")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
    classpath("com.google.firebase:perf-plugin:1.4.2")
    classpath(libs.android.gradlePlugin)
    classpath(libs.kotlin.gradlePlugin)
    classpath(libs.kotlin.serializationPlugin)
    classpath(libs.hilt.gradlePlugin)
  }
}

plugins {
  alias(libs.plugins.secrets) apply false
}

task("clean", Delete::class) {
  delete(rootProject.buildDir)
}
