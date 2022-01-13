plugins {
    id("library-setting-plugin")
    id("compose-plugin")
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    // AndroidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.UI.preference)

    // Compose
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.activity)

}