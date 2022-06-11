plugins {
    id("keelim.android.application")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
}

android {
    defaultConfig {

    }
    buildTypes {
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    useLibrary("android.test.mock")
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.material3)
    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.lifecycle.process)

    implementation(libs.androidx.profileinstaller)

    implementation(libs.play.services.ad)
    implementation(libs.play.services.oss)

    implementation(libs.timber)
    implementation(libs.apache.math)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performances)

    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
}




