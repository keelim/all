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
    implementation(projects.feature.uiSetting.api)

    implementation(projects.feature.settingsCore.impl)
    implementation(projects.feature.settingsTheme.impl)
    implementation(projects.feature.settingsNotification.impl)
    implementation(projects.feature.settingsAlarm.impl)
    implementation(projects.feature.settingsDevice.impl)
    implementation(projects.feature.settingsAdmin.impl)
    implementation(projects.feature.settingsLab.impl)
    implementation(projects.feature.uiWeb.api)

    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.core.model)
    implementation(projects.core.resource)
    implementation(projects.shared)

    implementation(projects.core.data.dataApi)
    implementation(projects.core.navigation)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation3.runtime)

    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.process)

    testImplementation(projects.core.testing)
}
