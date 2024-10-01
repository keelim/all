plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
}

android {
    namespace = "com.keelim.widget.glance"
}

dependencies {
    implementation(libs.androidx.compose.glance)
    implementation(libs.androidx.compose.glance.material3)
    implementation(libs.glance.tools.appwidget.host)
}
