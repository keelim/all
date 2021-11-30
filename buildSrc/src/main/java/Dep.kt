object Play {
    const val play_location = "com.google.android.gms:play-services-location:18.0.0"
    const val play_map = "com.google.android.gms:play-services-maps:17.0.1"
    const val play_ads = "com.google.android.gms:play-services-ads:20.2.0"
    const val play_core = "com.google.android.play:core:1.10.0"
    const val maps_sdk = "com.google.maps.android:maps-ktx:3.0.1"
    const val play_auth = "com.google.android.gms:play-services-auth:19.2.0"
}

object Coil {
    private const val version = "1.3.2"
    const val coil = "io.coil-kt:coil:$version"
    const val compose = "io.coil-kt:coil-compose:$version"
}

object Rx {
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val rxjava = "io.reactivex.rxjava2:rxjava:2.2.21"
    const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"
}

object Kotlin {
    const val version = "1.5.21"
    const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0"
}

object Coroutines {
    private const val coroutinesVersion = "1.5.0"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
}

object SquareUp {
    private const val version = "4.9.1"
    private const val rversion = "2.9.0"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.5"
    const val core = "com.squareup.okhttp3:okhttp:$version"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    const val urlconnection = "com.squareup.okhttp3:okhttp-urlconnection:$version"
    const val retrofit = "com.squareup.retrofit2:retrofit:$rversion"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:$rversion"
}

object Room {
    private const val roomVersion = "2.3.0-alpha02"
    const val runtime = "androidx.room:room-runtime:$roomVersion"
    const val compiler = "androidx.room:room-compiler:$roomVersion"
    const val ktx = "androidx.room:room-ktx:$roomVersion"
    const val testing = "androidx.room:room-testing:$roomVersion"
}

object LifeCycle {
    private const val lifecycleVersion = "2.3.1"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
    const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val services = "androidx.lifecycle:lifecycle-services:$lifecycleVersion"
}

object DataStore {
    const val dataStorePreferences = "1.0.0"
    const val preferences = "androidx.datastore:datastore-preferences:${dataStorePreferences}"
}

object Hilt {
    const val version = "2.36"
    const val android = "com.google.dagger:hilt-android:${version}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${version}"
    const val hilt_common = "androidx.hilt:hilt-common:$version"
    const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${version}"

    const val testing = "com.google.dagger:hilt-android-testing:${version}"
    const val android_hilt_compiler = "androidx.hilt:hilt-compiler:${version}"
}

object AndroidX {
    const val annotation = "androidx.annotation:annotation:1.2.0-alpha01"

    private const val activityVersion = "1.3.1"
    const val activity = "androidx.activity:activity:$activityVersion"
    const val activity_ktx = "androidx.activity:activity-ktx:$activityVersion"

    const val appcompat = "androidx.appcompat:appcompat:1.4.0-alpha03"
    const val core_ktx = "androidx.core:core-ktx:1.7.0-alpha01"

    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:2.4.0-beta02"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:2.4.0-beta02"

    const val browser = "androidx.browser:browser:1.3.0"
    private const val fragmentVersion = "1.4.0-alpha07"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:1.3.0"
}

object UI {
    const val browser = "androidx.browser:browser:1.3.0-alpha06"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.0"
    const val drawerlayout = "androidx.drawerlayout:drawerlayout:1.1.1"
    const val material = "com.google.android.material:material:1.4.0"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
    const val palette = "androidx.palette:palette:1.0.0"
    const val preference_ktx = "androidx.preference:preference-ktx:1.1.1"

    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
    const val viewPager = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
    const val recycler_selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
}

object Compose {
    private const val compose_version = "1.0.1"
    const val compose_ui = "androidx.compose.ui:ui:$compose_version"
    const val compose_material = "androidx.compose.material:material:$compose_version"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:$compose_version"
    const val foundation = "androidx.compose.foundation:foundation:$compose_version"
    const val compose_icon = "androidx.compose.material:material-icons-core:$compose_version"
    const val expand_icon = "androidx.compose.material:material-icons-extended:$compose_version"
    const val runtime_livedata = "androidx.compose.runtime:runtime-livedata:$compose_version"
    const val compose_junit = "androidx.compose.ui:ui-test-junit4:$compose_version"
}

object AppTest {
    const val junit = "junit:junit:4.13.2"
    const val assertJ = "org.assertj:assertj-core:3.17.2"
    const val mockito = "org.mockito:mockito-core:3.5.13"
    const val androidJunit = "androidx.test.ext:junit:1.1.3"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
}

object Glide {
    private const val version = "4.12.0"
    const val core = "com.github.bumptech.glide:glide:$version"
    const val compiler = "com.github.bumptech.glide:compiler:$version"
}