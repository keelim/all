plugins {
    id("keelim.android.application")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("androidx.navigation.safeargs.kotlin") version ("2.4.2")
}

android {
    defaultConfig {
        applicationId = "com.keelim.nandadiagnosis"
        versionCode = 53
        versionName = "1.0.53"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    useLibrary("android.test.mock")
    buildFeatures { dataBinding = true }
    namespace = "com.keelim.nandadiagnosis"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":compose"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":features:ui-setting"))

    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.google.firebase:firebase-config-ktx")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.lifecycle.rutime)

    implementation(libs.play.services.ad)
    implementation(libs.play.services.oss)

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.paging.common)
    implementation(libs.timber)
    implementation(libs.coil.kt)
    implementation(libs.androidx.startup)
}
