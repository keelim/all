plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.secrets)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

secrets {
    defaultPropertiesFileName = "local.defaults.properties"
}

android {
    namespace = "com.keelim.nandadiagnosis"

    defaultConfig {
        applicationId = "com.keelim.nandadiagnosis"
    }
}


dependencies {
    implementation(projects.core.commonAndroid)
    implementation(projects.core.data)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.ads.mobile.sdk)

    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
}
