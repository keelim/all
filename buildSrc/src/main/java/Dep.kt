object Coil {
    const val coil = "io.coil-kt:coil:1.2.1"
}

object DataStore {
    const val dataStorePreferences = "1.0.0-beta01"
    const val preferences =
        "androidx.datastore:datastore-preferences:${dataStorePreferences}"
}


object Play {
    const val play_location = "com.google.android.gms:play-services-location:18.0.0"
    const val play_map = "com.google.android.gms:play-services-maps:17.0.0"
    const val play_ads = "com.google.android.gms:play-services-ads:20.0.0"
    const val play_core = "com.google.android.play:core:1.10.0"
}

object Rx {
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val rxjava = "io.reactivex.rxjava2:rxjava:2.2.21"
    const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"
}

object SquareUp {
    private const val version = "4.9.1"
    private const val rversion = "2.9.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.5"
    const val core = "com.squareup.okhttp3:okhttp:$version"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    const val urlconnection = "com.squareup.okhttp3:okhttp-urlconnection:$version"
    const val retrofit = "com.squareup.retrofit2:retrofit:$rversion"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:$rversion"
}

object Room {
    private const val roomVersion = "2.3.0"
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
}

object Hilt {
    const val version = "2.37"
    const val android = "com.google.dagger:hilt-android:${version}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${version}"
    const val hilt_common = "androidx.hilt:hilt-common:$version"
    const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${version}"

    const val testing = "com.google.dagger:hilt-android-testing:${version}"
    const val android_hilt_compiler = "androidx.hilt:hilt-compiler:${version}"
}

object Compose {
    private const val compose_version = "1.0.0-rc01"
    const val compose_ui = "androidx.compose.ui:ui:$compose_version"
    const val compose_material = "androidx.compose.material:material:$compose_version"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:$compose_version"
}

object Kotlin {
    const val version = "1.5.10"
    const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

    object Coroutines {
        private const val coroutinesVersion = "1.5.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
    }

    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0"
}

object AndroidX {
    const val annotation = "androidx.annotation:annotation:1.2.0-alpha01"

    private const val activityVersion = "1.2.3"
    const val activity = "androidx.activity:activity:$activityVersion"
    const val activity_ktx = "androidx.activity:activity-ktx:$activityVersion"

    private const val fragmentVersion = "1.3.4"
    const val fragment = "androidx.fragment:fragment:$fragmentVersion"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"

    const val appcompat = "androidx.appcompat:appcompat:1.3.0-rc01"
    const val core_ktx = "androidx.core:core-ktx:1.6.0-alpha03"

    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:2.3.5"
}

object UI {
    const val browser = "androidx.browser:browser:1.3.0-alpha06"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val drawerlayout = "androidx.drawerlayout:drawerlayout:1.1.1"
    const val material = "com.google.android.material:material:1.4.0-alpha02"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
    const val palette = "androidx.palette:palette:1.0.0"
    const val preference_ktx = "androidx.preference:preference-ktx:1.1.1"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0"
    const val recycler_selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
    const val viewPager = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
}

object AppTest {
    const val junit = "junit:junit:4.13"
    const val assertJ = "org.assertj:assertj-core:3.17.2"
    const val mockito = "org.mockito:mockito-core:3.5.13"
    const val androidJunit = "androidx.test.ext:junit:1.1.2"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
}


object Glide {
    private const val version = "4.12.0"
    const val core = "com.github.bumptech.glide:glide:$version"
    const val compiler = "com.github.bumptech.glide:compiler:$version"


    object Dep {
        object GradlePlugin {
            const val androidStudioVersion = "4.2.0-alpha14"
            const val android = "com.android.tools.build:gradle:$androidStudioVersion"
            const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
            const val kotlinSerialization =
                "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
            const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:9.4.0"
            const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.version}"
        }


        object Dagger {
            private const val version = "2.29.1"
            const val dagger = "com.google.dagger:dagger:$version"
            const val compiler = "com.google.dagger:dagger-compiler:$version"
        }




        const val jsoup = "org.jsoup:jsoup:1.13.1"

    }


}