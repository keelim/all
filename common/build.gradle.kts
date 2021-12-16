plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

dependencies {
    implementation(projects.data)
    implementation(projects.compose)
    implementation(Dep2.inject)
    implementation(LifeCycle.runtime)
    implementation("androidx.activity:activity-ktx:1.3.1")
}

kapt {
    useBuildCache = true
}