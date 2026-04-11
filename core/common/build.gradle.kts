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
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.material.themAdapter)
    implementation(libs.timber)
    implementation(libs.zxing)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.rules)
}
