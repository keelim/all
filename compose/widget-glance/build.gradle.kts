plugins {
    id("keelim.android.library")
    id("keelim.android.library.compose")
    id("keelim.android.library.jacoco")
}

android {
    namespace = "com.keelim.widget.glance"
}

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.compose.glance)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
}
