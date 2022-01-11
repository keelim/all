plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

dependencies {
    implementation(projects.data)
    implementation(projects.compose)
    implementation(AndroidX.LifeCycle.runtime)
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.fragment_ktx)
}