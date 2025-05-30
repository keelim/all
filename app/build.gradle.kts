plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.compose)
}

android {
    namespace = "com.google.ai.edge.gallery"
    compileSdk = 35

    defaultConfig {
        // Don't change to com.google.ai.edge.gallery yet.
        applicationId = "com.google.aiedge.gallery"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.3"

        // Needed for HuggingFace auth workflows.
        manifestPlaceholders["appAuthRedirectScheme"] = "com.google.ai.edge.gallery.oauth"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material.icon.extended)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.com.google.code.gson)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.mediapipe.tasks.text)
    implementation(libs.mediapipe.tasks.genai)
    implementation(libs.mediapipe.tasks.imagegen)
    implementation(libs.commonmark)
    implementation(libs.richtext)
    implementation(libs.tflite)
    implementation(libs.tflite.gpu)
    implementation(libs.tflite.support)
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)
    implementation(libs.openid.appauth)
    implementation(libs.androidx.splashscreen)
}
