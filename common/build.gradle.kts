plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

listOf(
        "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}


dependencies {
    implementation(LifeCycle.viewmodel)
    implementation(LifeCycle.livedata)
    implementation(UI.material)
}
