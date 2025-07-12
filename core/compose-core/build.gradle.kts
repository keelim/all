plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.keelim.android.showkase)
}

android {
    namespace = "com.keelim.compose.core"
    lint {
        disable += "SuspiciousModifierThen"
    }
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.emoji2.emojipicker)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.inapp.update)
    implementation(libs.material.themAdapter)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.androidx.media.compose)
    implementation(libs.androidx.media.exoplayer)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    androidTestImplementation(libs.androidx.compose.ui.test)

    api(projects.core.component)
}
