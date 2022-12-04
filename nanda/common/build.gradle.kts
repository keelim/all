plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
}

dependencies {
    implementation(Dep.AndroidX.lifecycle.runtime)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.activity.ktx)
}
android {
    namespace = "com.keelim.common"
}
