plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.firebase)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.nandadiagnosis"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        // create("nanda-benchmark") {
        //     signingConfig = signingConfigs.getByName("debug")
        //     matchingFallbacks += listOf("release")
        //     isDebuggable = false
        // }
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.nandadiagnosis"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.commonAndroid)
    implementation(projects.core.composeCore)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.shared)
    implementation(projects.feature.uiSetting)

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.work.ktx)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
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
    implementation(libs.deeplinkdispatch)
    ksp(libs.deeplinkdispatch.processor)
}
