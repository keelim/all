plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.datastore.preference)

    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    implementation(Dep.Network.Retrofit.retrofit)
    implementation(Dep.Network.Retrofit.retrofit_gson)
    implementation(Dep.Network.Retrofit.retrofit_moshi)

    implementation(Dep.timber)

    implementation(Dep.Play.maps_sdk)

    implementation(Dep.AndroidX.room.runtime)
    implementation(Dep.AndroidX.room.ktx)
    kapt(Dep.AndroidX.room.compiler)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.play)

    implementation(platform(Dep.Firebase.platform))
    implementation("com.google.firebase:firebase-storage-ktx")


    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
}

