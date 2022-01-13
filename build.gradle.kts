buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.0-rc01")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.4")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}