plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

android {

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha04"
    }
}

dependencies {
    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_tooling)
    implementation(Compose.foundation)
    implementation(Compose.compose_material)
    implementation(Compose.compose_icon)
    implementation(Compose.expand_icon)
    implementation(Compose.runtime_livedata)
    implementation(Coil.coil)
    implementation(Coil.compose)
    androidTestImplementation(Compose.compose_junit)

    implementation(Dep2.AndroidX.Activity.compose)
    implementation(Dep2.Compose.themeAdapter)
}

kapt {
    useBuildCache = true
}