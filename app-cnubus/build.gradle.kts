plugins {
    id("keelim.android.application")
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

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")


    implementation("com.jakewharton:process-phoenix:2.1.2")
    implementation("com.airbnb.android:lottie:5.2.0")

    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("androidx.profileinstaller:profileinstaller:1.2.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.0")

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)
}
