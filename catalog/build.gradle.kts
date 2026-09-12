plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        applicationId = "com.keelim.catalog"
    }
    namespace = "com.keelim.catalog"
}

ksp {
    arg("skipPrivatePreviews", "true")
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(project.dependencies.platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.material.themeAdapter)

    implementation("com.airbnb.android:showkase:1.0.5")
    implementation("com.airbnb.android:showkase-annotation:1.0.5")
    ksp("com.airbnb.android:showkase-processor:1.0.5")
}
