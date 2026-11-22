plugins {
    alias(libs.plugins.keelim.android.feature.settings)
}

android {
    namespace = "com.keelim.settings.lab"
}

dependencies {
    implementation(projects.feature.settingsLab.api)
}
