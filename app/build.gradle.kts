import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("application-setting-plugin")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId = ProjectConfigurations.applicationID
        versionCode = ProjectConfigurations.versionCode
        versionName = ProjectConfigurations.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    val key: String = gradleLocalProperties(rootDir).getProperty("UNIT")

    buildTypes {
        defaultConfig{
            buildConfigField("String", "key", key)
        }
    }
    useLibrary("android.test.mock")
}

dependencies {
    implementation(projects.data)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.WorkManager.work)
    implementation(Dep.AndroidX.hilt.common)
    implementation(Dep.AndroidX.hilt.work)

    implementation(platform(Dep.Firebase.platform))
    implementation(Dep.Firebase.analytics)
    implementation(Dep.Firebase.crashlytics)

    implementation(Dep.Play.ad)
    implementation(Dep.Play.oss)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.other.math)
    implementation(Dep.AndroidX.lifecycle.runtime)

    implementation(Dep.AndroidX.navigation.ui)
    implementation(Dep.AndroidX.navigation.fragment)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}




