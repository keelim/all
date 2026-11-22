plugins {
    alias(libs.plugins.keelim.android.feature.settings)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.keelim.settings.core"
}

dependencies {
    implementation(projects.feature.settingsCore.api)
    implementation(projects.feature.settingsTheme.api)
    implementation(projects.feature.settingsNotification.api)
    implementation(projects.feature.settingsAlarm.api)
    implementation(projects.feature.settingsDevice.api)
    implementation(projects.feature.settingsAdmin.api)
    implementation(projects.feature.settingsLab.api)

    testImplementation(libs.kotlinx.serialization.json)
}
