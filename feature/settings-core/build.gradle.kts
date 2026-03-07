plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.keelim.settings.core"
}

dependencies {
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
}
