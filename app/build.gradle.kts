plugins {
    id("com.android.application")
    id("kotlin-android")
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

android {
    compileSdk = ProjectConfigurations.compileSdk

    defaultConfig {
        applicationId = ProjectConfigurations.applicationId
        minSdk = ProjectConfigurations.minSdk
        targetSdk = ProjectConfigurations.targetSdk
        versionCode = ProjectConfigurations.versionCode
        versionName = ProjectConfigurations.versionName
        
    }

    buildTypes {
        getByName("debug") {
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfigurations.composeCompiler
    }

    kapt {
        correctErrorTypes = true
        useBuildCache = true
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
    implementation(Play.play_core)
    implementation(Play.play_ads)
    implementation(Play.play_location)
    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)
    implementation(SquareUp.timber)
    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)
    implementation(Coil.coil)
    implementation(DataStore.preferences)
    implementation(LifeCycle.viewmodel)
    implementation(LifeCycle.livedata)
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_tooling)
    implementation(Compose.foundation)
    implementation(Compose.compose_material)
    implementation(Compose.compose_icon)
    implementation(Compose.expand_icon)
    implementation(Compose.runtime_livedata)
    androidTestImplementation(Compose.compose_junit)
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")

    implementation("androidx.core:core-splashscreen:1.0.0-alpha02")
    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    implementation(SquareUp.retrofit)
}

apply(from = "$rootDir/spotless.gradle")



