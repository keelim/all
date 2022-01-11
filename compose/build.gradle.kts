plugins {
    id("library-setting-plugin")
    id("compose-setting-plugin")
    id("kotlin-kapt")
}

dependencies {
    implementation(Dep2.Compose.ui)
    implementation(Dep2.Compose.material)
    implementation(Dep2.Compose.tooling)
    implementation(Dep2.Compose.themeAdapter)
    implementation(Dep2.Compose.liveData)
    implementation(Coil.coil)
    implementation(Coil.compose)
    implementation(Dep2.AndroidX.Activity.compose)
    implementation(Dep2.Compose.themeAdapter)
    implementation(Dep.Compose.glance)
}

