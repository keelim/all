plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    id("keelim.android.application.jacoco")
    id("keelim.android.hilt")
}

android {
    defaultConfig {
        applicationId = "com.keelim.mygrade"
        buildTypes {
            create("my-grade-benchmark") {
                signingConfig = signingConfigs.getByName("debug")
                matchingFallbacks += listOf("release")
                isDebuggable = false
            }
        }
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.mygrade"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":compose"))
    implementation(project(":data"))

    implementation("com.google.modernstorage:modernstorage-permissions")
    implementation("com.google.modernstorage:modernstorage-photopicker")
    implementation("com.google.modernstorage:modernstorage-storage")
    implementation("com.squareup.okio:okio")

    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.rutime)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.metrics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.apache.math)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)
    implementation(libs.firebase.config)
    implementation(libs.hilt.ext.work)
    implementation(libs.inapp.update)
    implementation(libs.material)
    implementation(libs.modernstorage.bom)
    implementation(libs.play.services.ad)
    implementation(libs.play.services.oss)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    kapt(libs.hilt.ext.compiler)
}




