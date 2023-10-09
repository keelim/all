plugins {
    id("keelim.android.library")
    id("keelim.android.library.jacoco")
    id("keelim.android.hilt")
    kotlin("plugin.parcelize")
}

android{
    namespace = "com.keelim.labs"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:compose-core"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.arrow.core)
    implementation(libs.material)
}

