plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    id("keelim.android.showkase")
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
    implementation(project(":core:compose-core"))
    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    
    ksp(libs.bundles.showkase)
}
