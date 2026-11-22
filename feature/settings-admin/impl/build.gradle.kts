plugins {
    alias(libs.plugins.keelim.android.feature.settings)
}

android {
    namespace = "com.keelim.settings.admin"
}

dependencies {
    implementation(projects.feature.settingsAdmin.api)
}
