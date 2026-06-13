plugins {
    alias(libs.plugins.keelim.android.library)
}

android {
    namespace = "com.keelim.common"
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.rules)
}
