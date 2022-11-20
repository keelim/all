plugins {
    id("library-setting-plugin")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android{
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.keelim.common"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":compose"))
    implementation(Dep.AndroidX.lifecycle.runtime)
    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.Coil.core)
    implementation(Dep.AndroidX.UI.material)

    implementation(Dep.Rx.rxJava)
    implementation(Dep.Rx.rxAndroid)
    implementation(Dep.Rx.rxKotlin)
    implementation(Dep.Rx.binding)

    implementation(Dep.timber)
}
