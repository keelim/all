plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.arducon"
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.arducon"
}

dependencies {

    implementation(projects.core.commonAndroid)

    implementation(projects.core.data)
    implementation(projects.core.navigation)

    implementation(projects.widget)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.profileinstaller)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.play.services.ad)
    implementation(libs.play.services.code.scanner)
    implementation(libs.timber)
    implementation(libs.jsoup)
}




