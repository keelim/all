plugins {
    alias(libs.plugins.keelim.android.feature.settings)
}

android {
    namespace = "com.keelim.settings.notification"
}

dependencies {
    implementation(projects.feature.settingsNotification.api)
}
