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

    implementation(Dep2.Compose.ui)
    implementation(Dep2.Compose.material)
    implementation(Dep2.Compose.tooling)
    implementation(Dep2.Compose.themeAdapter)
    implementation(Dep2.Compose.liveData)
    implementation(Dep.Compose.viewModel)
    implementation(AndroidX.LifeCycle.livedata)

    implementation(Dep2.Coil.compose)

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)
    
    implementation(Dep2.timber)

    testImplementation(Dep2.Test.junit)
    androidTestImplementation(Dep2.Test.junitExt)
    androidTestImplementation(Dep2.Test.espresso)
}