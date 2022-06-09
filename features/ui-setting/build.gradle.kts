plugins {
    id("library-setting-plugin")
    kotlin("kapt")
    id("compose-setting-plugin")
    id("dagger.hilt.android.plugin")
}


dependencies {
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material3)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)
    implementation(Dep.AndroidX.Compose.viewModel)
    implementation(Dep.AndroidX.lifecycle.livedata)
    implementation(Dep.AndroidX.lifecycle.runtime)
    implementation(Dep.AndroidX.Compose.activity)

    implementation(Dep.Coil.compose)
    implementation(Dep.Coil.core)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}
