plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.keelim.android.application.room)
    kotlin("plugin.parcelize")
    kotlin("plugin.serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    // alias(libs.plugins.protobuf)
}

// protobuf {
//     protoc {
//         // TODO: 고쳐야 하는 부분
//         artifact = "com.google.protobuf:protoc:3.25.0"
//     }
//     generateProtoTasks {
//         all().forEach { task -> task.builtins { register("java") { option("lite") } } }
//     }
// }

android { namespace = "com.keelim.data" }

dependencies {
    implementation(projects.core.common)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.paging.common)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.generativeai)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.serialization.kotlinx.json)
    implementation(libs.ktor.client.websockets)
    implementation(platform(libs.okio.bom))
    implementation(libs.okio)
    implementation(libs.play.services.maps)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))

    testImplementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
