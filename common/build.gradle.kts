plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
}

dependencies {
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.fragment.ktx)
}
