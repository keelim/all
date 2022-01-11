import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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
    id ("com.google.secrets_gradle_plugin") version("0.5")
}

val key: String = gradleLocalProperties(rootDir).getProperty("APPCENTER_KEY")

android {

    buildTypes {

        getByName("debug") {
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
            buildConfigField("String", "APPCENTER_KEY", key)
        }
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
            buildConfigField("String", "APPCENTER_KEY", key)
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

    implementation(platform("com.google.firebase:firebase-bom:28.2.0"))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")

    implementation(AndroidX.navigation_ui)
    implementation(AndroidX.navigation_fragment)
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)

    implementation(UI.constraintLayout)
    implementation(UI.material)
    implementation(UI.preference_ktx)
    implementation(UI.recyclerview)
    implementation(UI.viewPager)

    implementation(Play.play_core)
    implementation(Play.play_ads)
    implementation(Play.play_location)
    implementation(Play.oss)

    implementation(SquareUp.timber)

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation(Coil.coil)

    implementation(DataStore.preferences)

    implementation(AndroidX.LifeCycle.viewmodel)
    implementation(AndroidX.LifeCycle.runtime)

    implementation(Dep2.Compose.ui)
    implementation(Dep2.Compose.material)
    implementation(Dep2.Compose.tooling)
    implementation(Dep2.Compose.themeAdapter)
    implementation(Dep2.Compose.liveData)

    implementation(AppCenter.analytics)
    implementation(AppCenter.crashes)

    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)

    implementation(SquareUp.retrofit)
    implementation(SquareUp.core)
}
