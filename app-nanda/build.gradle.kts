plugins {
    id("keelim.android.application")
    id("keelim.android.application.firebase")
    id("keelim.android.application.compose")
    id("keelim.android.application.jacoco")
    id("keelim.android.hilt")
    id("androidx.navigation.safeargs.kotlin") version ("2.5.3")
}

android {
    defaultConfig {
        applicationId = "com.keelim.nandadiagnosis"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        create("nanda-benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    useLibrary("android.test.mock")
    buildFeatures { dataBinding = true }
    namespace = "com.keelim.nandadiagnosis"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":features:ui-setting"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.config)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.lifecycle.rutime)

    implementation(libs.play.services.ad)
    implementation(libs.play.services.oss)
    implementation(libs.play.services.auth)

    implementation(libs.inapp.update)

    implementation(libs.androidx.work.ktx)
    // hilt
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.paging.common)
    implementation(libs.timber)
    implementation(libs.coil.kt)
    implementation(libs.androidx.startup)
    // compose
    implementation(libs.androidx.activity.compose)
}
