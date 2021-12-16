plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("dagger.hilt.android.plugin")
}


android {
    compileSdk =  ProjectConfigurations.compileSdk
    defaultConfig {
        applicationId = ProjectConfigurations.applicationId
        minSdk = ProjectConfigurations.minSdk
        targetSdk = ProjectConfigurations.targetSdk
        versionCode = ProjectConfigurations.versionCode
        versionName = ProjectConfigurations.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            firebaseAppDistribution {
                testers = "kimh00335@gmail.com"
            }
        }
        debug {
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
        useIR = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha05"
    }
    namespace = "com.keelim.nandadiagnosis"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(projects.common)
    implementation(projects.compose)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.features.uiSetting)
    implementation(projects.features.player)

    implementation(platform("com.google.firebase:firebase-bom:28.3.1"))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.google.firebase:firebase-config-ktx")

    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.fragment_ktx)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.navigation_ui)
    implementation(AndroidX.navigation_fragment)

    implementation(LifeCycle.viewmodel)
    implementation(LifeCycle.livedata)
    implementation(LifeCycle.runtime)

    implementation(UI.constraintLayout)
    implementation(UI.recycler_selection)
    implementation(UI.recyclerview)
    implementation(UI.preference_ktx)
    implementation(UI.material)
    implementation(UI.swiperefreshlayout)

    implementation(Play.play_core)
    implementation(Play.play_ads)

    implementation(SquareUp.retrofit)
    implementation(SquareUp.retrofit_gson)
    implementation(SquareUp.timber)

    implementation(Hilt.android)
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")
    kapt(Hilt.hilt_compiler)
    implementation(Play.play_auth)

    testImplementation(AppTest.junit)
    androidTestImplementation(AppTest.androidJunit)
    androidTestImplementation(AppTest.espressoCore)

    implementation(Coil.coil)

    implementation(SquareUp.core)
    implementation(SquareUp.loggingInterceptor)
    implementation(SquareUp.urlconnection)
    implementation(SquareUp.retrofit)
    implementation(SquareUp.retrofit_gson)
    implementation(SquareUp.timber)

    implementation(Kotlin.stdlibJvm)

    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_tooling)
    implementation(Compose.foundation)
    implementation(Compose.compose_material)
    implementation(Compose.compose_icon)
    implementation(Compose.expand_icon)
    implementation(Compose.runtime_livedata)
    androidTestImplementation(Compose.compose_junit)

    implementation("androidx.startup:startup-runtime:1.1.0")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("android.arch.work:work-runtime-ktx:1.0.1")
    implementation("androidx.paging:paging-common-ktx:3.1.0")
}
apply(from = "$rootDir/spotless.gradle")

kapt {
    useBuildCache = true
}