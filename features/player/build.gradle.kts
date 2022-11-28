plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.common)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)

    implementation(Dep.Coil.core)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.Player.exoplayer)
}
android {
    namespace = "com.keelim.player"
}


