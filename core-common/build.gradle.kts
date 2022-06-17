plugins {
    id("keelim.android.library")
    kotlin("kapt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.fragment.ktx)
    implementation(libs.material3)
}
