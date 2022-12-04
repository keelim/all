plugins {
    id("application-setting-plugin")
    id("compose-setting-plugin")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services") version("4.3.14")
    id("com.google.firebase.firebase-perf") version("1.4.1")
    id("com.google.firebase.crashlytics") version("2.9.0")
    id("com.google.firebase.appdistribution") version("3.0.2")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin") version("2.4.2")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version("2.0.1")
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
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
        }
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
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
    implementation(project(":features:ui-map"))
    implementation(project(":features:ui-setting"))
    implementation(project(":features:labs"))

    implementation(platform(Dep.Firebase.platform))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.firebaseui:firebase-ui-auth:8.0.1")

    implementation(Dep.AndroidX.navigation.ui)
    implementation(Dep.AndroidX.navigation.fragment)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)

    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.UI.recyclerview)
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.AndroidX.UI.viewPager)

    implementation(Dep.Play.core)
    implementation(Dep.Play.ad)
    implementation(Dep.Play.oss)
    implementation(Dep.Play.location)
    implementation(Dep.Play.play_auth)

    implementation(Dep.timber)

    implementation(Dep.Dagger.Hilt.android)
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.Coil.core)
    implementation(Dep.AndroidX.datastore.preference)

    implementation(Dep.AndroidX.lifecycle.viewModelKtx)
    implementation(Dep.AndroidX.lifecycle.service)
    implementation(Dep.AndroidX.lifecycle.runtime)
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)

    implementation(Dep.AppCenter.analytics)
    implementation(Dep.AppCenter.crashes)
    implementation(Dep.Network.Retrofit.retrofit)
    implementation(Dep.Network.Moshi.moshi_kotlin)

    implementation(Dep.Rx.rxJava)
    implementation(Dep.Rx.rxAndroid)
    implementation(Dep.Rx.rxKotlin)
    implementation(Dep.Rx.binding)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation("com.jakewharton:process-phoenix:2.1.2")
    implementation("com.airbnb.android:lottie:5.2.0")

    implementation(Dep.AndroidX.WorkManager.work)
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Kotlin.coroutines.test)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)

    implementation("androidx.profileinstaller:profileinstaller:1.2.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.0")
}
