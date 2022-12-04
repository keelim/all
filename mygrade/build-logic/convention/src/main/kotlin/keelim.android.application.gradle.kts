
import com.keelim.builds.ProjectConfigurations
import com.keelim.builds.configureKotlinAndroid

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    configureKotlinAndroid(this)

    defaultConfig {
        targetSdk = ProjectConfigurations.targetSdk
        applicationId = ProjectConfigurations.applicationID
        versionCode = ProjectConfigurations.versionCode
        versionName = ProjectConfigurations.versionName
    }

    buildFeatures.viewBinding = true

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }
}
