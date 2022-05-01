plugins {
    id("application-setting-plugin")
    id("compose-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.firebase-perf")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.secrets_gradle_plugin") version ("0.6.1")
    id("org.jetbrains.kotlin.android")
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
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.compose)
    implementation(projects.common)
    implementation(projects.features.uiMap)
    implementation(projects.features.uiSetting)
    implementation(projects.features.labs)

    implementation(platform(Dep.Firebase.platform))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")

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
    implementation("com.airbnb.android:lottie:5.0.3")

    implementation(Dep.AndroidX.WorkManager.work)
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Kotlin.coroutines.test)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}
