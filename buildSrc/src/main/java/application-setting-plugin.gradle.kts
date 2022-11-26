import BuildTypeRelease.isMinifyEnabled
import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.application")
    id("android-setting-plugin")
    id("kotlin-android")
    id("kotlin-setting-plugin")
}

configure<BaseExtension> {
    buildFeatures.viewBinding = true
    buildFeatures.compose = true

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }
}
