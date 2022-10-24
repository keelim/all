plugins {
    id("application-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
}

android {
    defaultConfig {
        applicationId = ProjectConfigurations.applicationId
        versionCode = ProjectConfigurations.versionCode
        versionName = ProjectConfigurations.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        defaultConfig {
        }
        getByName("release") {
        }
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.activity:activity-ktx:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.5.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.lifecycle:lifecycle-process:2.3.1")
    implementation("com.google.android.play:core-ktx:1.8.1")

    implementation(platform("com.google.firebase:firebase-bom:30.5.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("androidx.paging:paging-common-ktx:3.1.0")
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")

    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)

    implementation(Kotlin.stdlibJvm)

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.google.android.gms:play-services-ads:20.5.0")

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")

    implementation(Coil.coil)
    implementation(Coil.coilGif)
    implementation(AndroidX.startup)
}
//apply(from = "$rootDir/spotless.gradle")
