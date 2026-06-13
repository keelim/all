plugins {
    alias(libs.plugins.keelim.android.feature.settings)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.keelim.settings.core"
}

dependencies {
    implementation(projects.feature.uiWeb)

    testImplementation(libs.kotlinx.serialization.json)
}
