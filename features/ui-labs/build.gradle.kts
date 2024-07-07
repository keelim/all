plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.hilt)
    kotlin("plugin.parcelize")
}

android{
    namespace = "com.keelim.labs"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
}

