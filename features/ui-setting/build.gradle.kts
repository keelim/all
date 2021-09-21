plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}


android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha03"
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":compose"))

    implementation(Dep2.AndroidX.Lifecycle.livedata)
    implementation(Dep2.AndroidX.Lifecycle.viewModelCompose)

    implementation(Dep2.Compose.ui)
    implementation(Dep2.Compose.material)
    implementation(Dep2.Compose.tooling)
    implementation(Dep2.Compose.themeAdapter)
    implementation(Dep2.Compose.liveData)

    implementation(Dep2.Coil.compose)

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)
    
    implementation(Dep2.timber)

    testImplementation(Dep2.Test.junit)
    androidTestImplementation(Dep2.Test.junitExt)
    androidTestImplementation(Dep2.Test.espresso)
}