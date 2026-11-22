plugins {
    alias(libs.plugins.keelim.android.feature.settings)
}

android {
    namespace = "com.keelim.settings.theme"
}

dependencies {
    implementation(projects.feature.settingsTheme.api)
}
