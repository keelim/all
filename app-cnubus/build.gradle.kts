plugins {
    id("keelim.android.application")
    id("keelim.android.application.compose")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    defaultConfig {
        applicationId = "com.keelim.cnubus"
        versionCode = 2118
        versionName = "2.1.18"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        defaultConfig {
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    namespace = "com.keelim.cnubus"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":compose"))
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":features:ui-map"))
    implementation(project(":features:ui-setting"))
    implementation(project(":features:ui-labs"))

    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.firebaseui:firebase-ui-auth:8.0.1")

    implementation("com.airbnb.android:lottie:5.2.0")

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(libs.hilt.android)
    implementation("androidx.preference:preference-ktx:1.2.0")
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.play.services.ad)
    implementation(libs.timber)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.androidx.compose.bom)
    implementation("androidx.compose.ui:ui:1.3.1")
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.coil.kt)
}
