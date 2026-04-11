plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.keelim.setting"
}

dependencies {

    api(projects.feature.settingsCore)
    api(projects.feature.settingsTheme)
    api(projects.feature.settingsNotification)
    api(projects.feature.settingsAlarm)
    api(projects.feature.settingsDevice)
    api(projects.feature.settingsAdmin)
    api(projects.feature.settingsLab)

    implementation(projects.core.commonAndroid)
    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.core.model)
    implementation(projects.core.resource)
    implementation(projects.shared)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)


    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(projects.feature.uiWeb)

    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.process)

    testImplementation(projects.core.testing)
}

