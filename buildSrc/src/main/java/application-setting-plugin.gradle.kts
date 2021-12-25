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
    signingConfigs {
        getByName("debug") {
        }
    }
    val pw: String = gradleLocalProperties(rootDir).getProperty("pw")
    val alias: String = gradleLocalProperties(rootDir).getProperty("alias")

    buildTypes {
        signingConfigs {
            getByName("release") {
                storeFile = project.rootProject.file("keystore.jks")
                storePassword = pw
                keyAlias = alias
                keyPassword = pw
            }
        }
        getByName("debug") {
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }
}