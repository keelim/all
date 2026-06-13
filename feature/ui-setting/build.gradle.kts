plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.keelim.setting"
}

dependencies {

    implementation(projects.feature.settingsCore)
    implementation(projects.feature.settingsTheme)
    implementation(projects.feature.settingsNotification)
    implementation(projects.feature.settingsAlarm)
    implementation(projects.feature.settingsDevice)
    implementation(projects.feature.settingsAdmin)
    implementation(projects.feature.settingsLab)
    implementation(projects.feature.uiWeb)

    implementation(projects.core.commonAndroid)
    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.core.model)
    implementation(projects.core.resource)
    implementation(projects.shared)

    implementation(projects.core.dataApi)
    implementation(projects.core.navigation)


    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation3.runtime)

    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.process)

    testImplementation(projects.core.testing)
}
