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
        val releaseVersion = "${libs.versions.versionCode.get()}${libs.versions.nandaVersionCodeSuffix.get()}"
        versionName = releaseVersion
        versionCode = releaseVersion.toInt()
    }
}


dependencies {
    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.core.data.dataImpl)
    implementation(projects.core.domain)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.fragment.ktx)
    implementation(libs.ads.mobile.sdk)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.core.testing)
}
