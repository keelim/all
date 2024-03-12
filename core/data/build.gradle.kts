plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    kotlin("plugin.parcelize")
    kotlin("plugin.serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}


android { namespace = "com.keelim.core.data" }

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.model)
    api(projects.core.network)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.paging.common)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.generativeai)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)
    implementation(platform(libs.okio.bom))
    implementation(libs.okio)
    implementation(libs.play.services.maps)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))

    testImplementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
