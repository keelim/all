buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.0-alpha05")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.4")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.0")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.0.4")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
