plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.keelim.android.hilt)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}


android { namespace = "com.keelim.core.data" }

dependencies {
    api(projects.core.model)
    api(projects.core.dataApi)
    api(projects.core.network)
    implementation(projects.shared)
    implementation(projects.core.common)
    implementation(projects.core.database)

    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.paging.common)
    implementation(libs.generativeai)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)
    implementation(libs.play.services.maps)
    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.appcheck.debug)

    testImplementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.play.services.time)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
