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
    implementation(project(":compose"))
    implementation(LifeCycle.viewmodel)
    implementation(LifeCycle.livedata)
    implementation(UI.material)

    implementation(Dep2.inject)
    implementation("androidx.activity:activity-ktx:1.3.1")
}

kapt {
    useBuildCache = true
}
