plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("compose-setting-plugin")
    id("dagger.hilt.android.plugin")
}


dependencies {
    implementation(projects.data)
    implementation(projects.compose)
    implementation(projects.domain)
    implementation(projects.common)

    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.materialAdapter)
    implementation(Dep.AndroidX.Compose.livedata)
    implementation(Dep.AndroidX.Compose.viewModel)

    implementation(Dep.AndroidX.lifecycle.livedata)

    implementation(Dep.Coil.compose)
    implementation(Dep.Coil.core)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}