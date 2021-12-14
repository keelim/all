plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

listOf(
    "android.gradle",
).forEach { file ->
    apply(from = "${rootDir}/gradle/${file}")
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(Billing.billing_ktx)
    implementation(AndroidX.appcompat)
    implementation(UI.material)
    implementation(UI.constraintLayout)

    implementation(Coil.coil)

    implementation(Hilt.android)
    kapt(Hilt.hilt_compiler)

    implementation(AndroidX.activity_ktx)
    implementation("com.google.android.exoplayer:exoplayer:2.16.1")
}

kapt {
    useBuildCache = true
}


