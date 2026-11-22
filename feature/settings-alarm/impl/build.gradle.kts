plugins {
    alias(libs.plugins.keelim.android.feature.settings)
}

android {
    namespace = "com.keelim.settings.alarm"
}

dependencies {
    implementation(projects.feature.settingsAlarm.api)
}
