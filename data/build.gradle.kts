plugins {
    id("keelim.android.library")
    kotlin("kapt")
    kotlin("plugin.parcelize")
    id("kotlinx-serialization")
    alias(libs.plugins.ksp)
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.paging.common)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)
    implementation(libs.timber)

    //datastore
    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    implementation(libs.play.services.maps)
}
android {
    namespace = "com.keelim.data"
}

