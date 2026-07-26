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
    implementation(projects.core.common)
    implementation(projects.core.commonAndroid)
    implementation(projects.core.component)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.fragment.ktx)
    implementation(libs.ads.mobile.sdk)

    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.core.testing)
}
