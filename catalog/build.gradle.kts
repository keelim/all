plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.showkase)
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
    implementation(projects.core.composeCore)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)

    ksp(libs.bundles.showkase)
}
