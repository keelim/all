buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:perf-plugin:1.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.7.1")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.2.2")
        classpath("com.google.firebase:firebase-appdistribution-gradle:3.0.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.0-alpha09")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.4")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}


