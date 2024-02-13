plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
    kotlin("plugin.serialization")
    alias(libs.plugins.keelim.android.showkase)
}

android {
    namespace = "com.keelim.compose.core"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.emoji2.emojipicker)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.material)
    implementation(libs.material.themAdapter)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    androidTestImplementation(libs.androidx.compose.ui.test)
}
