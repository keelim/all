plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.firebase)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.comssa"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // create("comssa-benchmark") {
        //     signingConfig = signingConfigs.getByName("debug")
        //     matchingFallbacks += listOf("release")
        //     isDebuggable = false
        // }
        getByName("release") {}
    }
    useLibrary("android.test.mock")
    namespace = "com.keelim.comssa"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.commonAndroid)
    implementation(projects.core.composeCore)
    implementation(projects.core.data)
    implementation(projects.shared)

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.haze)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.datetime)
    implementation(libs.play.services.ad)
    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))
}
