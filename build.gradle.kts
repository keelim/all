buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.1.2")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
