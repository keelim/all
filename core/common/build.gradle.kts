plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    namespace = "com.keelim.common"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.fragment.ktx)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.material.themAdapter)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
}

