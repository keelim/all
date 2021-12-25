import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("android-setting-plugin")
    id("kotlin-android")
    id("kotlin-setting-plugin")
}

configure<BaseExtension> {
    buildFeatures.viewBinding = true



    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }
}