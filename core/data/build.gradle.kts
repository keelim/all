plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
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
    api(projects.core.common)
    api(projects.core.model)
    api(projects.core.database)

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
