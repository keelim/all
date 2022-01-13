plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
}

dependencies {
    implementation(Dep.AndroidX.lifecycle.runtime)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.lifecycle.runtime)
}