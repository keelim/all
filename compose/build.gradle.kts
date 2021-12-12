plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}
android{
    compileSdk = 31
    defaultConfig{
        targetSdk = 31
        minSdk = 24
    }
    buildFeatures{
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha05"
    }
}

dependencies {
    implementation("androidx.compose.ui:ui:1.0.5")
// Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.0.5")
// Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.0.5")
// Material Design
    implementation("androidx.compose.material:material:1.0.5")
// Material design icons
    implementation("androidx.compose.material:material-icons-core:1.0.5")
    implementation("androidx.compose.material:material-icons-extended:1.0.5")
// Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:1.0.5")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.0.5")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")

// UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.5")
}