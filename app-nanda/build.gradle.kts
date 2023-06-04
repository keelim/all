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
    namespace = "com.keelim.nandadiagnosis"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":features:ui-setting"))

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.work.ktx)
    implementation(libs.coil.kt)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.config)
    implementation(libs.firebase.database)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.messaging)
    implementation(libs.hilt.ext.work)
    implementation(libs.inapp.update)
    implementation(libs.play.services.ad)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    kapt(libs.hilt.ext.compiler)
}
