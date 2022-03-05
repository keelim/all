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
    id ("com.google.secrets_gradle_plugin") version("0.6.1")
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
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.compose)
    implementation(projects.common)
    implementation(projects.features.uiMap)
    implementation(projects.features.uiSetting)

    implementation(platform(Dep.Firebase.platform))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")

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

    implementation(Dep.timber)

    implementation(Dep.Dagger.Hilt.android)
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

    implementation("com.jakewharton:process-phoenix:2.1.2")

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Kotlin.coroutines.test)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)

}
