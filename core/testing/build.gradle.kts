plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    namespace = "com.keelim.testing"
}

dependencies {
    api(libs.accompanist.testharness)
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
    api(libs.roborazzi)
    api(libs.robolectric.shadows)
    api(libs.truth)
    api(libs.turbine)

    debugApi(libs.androidx.compose.ui.testManifest)
    implementation(libs.kotlinx.datetime)

    implementation(projects.core.common)
}
