buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.5")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.0")
        classpath("com.google.firebase:perf-plugin:1.4.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
