plugins {
    id("keelim.android.library")
    kotlin("kapt")
}

dependencies {
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.fragment.ktx)
    implementation(libs.material3)
}
