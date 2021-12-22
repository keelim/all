import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.application")
    id("android-setting-plugin")
    id("kotlin-android")
    id("kotlin-setting-plugin")
}

configure<BaseExtension> {
    buildFeatures.viewBinding = true
    signingConfigs {
        getByName("debug") {
        }
    }

    buildTypes {
        getByName("debug") {
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }
}