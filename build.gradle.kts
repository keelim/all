buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://naver.jfrog.io/artifactory/maven/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.0-alpha01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:perf-plugin:1.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.7.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:5.14.3")
        classpath("com.google.firebase:firebase-appdistribution-gradle:2.1.3")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.0-alpha09")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.4")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}


