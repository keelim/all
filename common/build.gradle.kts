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
    implementation(LifeCycle.runtime)
    implementation(UI.material)
    implementation(AndroidX.activity_ktx)
}

kapt {
    useBuildCache = true
}
