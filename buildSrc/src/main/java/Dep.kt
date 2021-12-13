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
    private const val lifecycleVersion = "2.4.0"
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
    const val version = "2.39.1"
    const val android = "com.google.dagger:hilt-android:${version}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${version}"
    const val hilt_common = "androidx.hilt:hilt-common:$version"
    const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${version}"

    const val testing = "com.google.dager:hilt-android-testing:${version}\"\n" +
            "    const val android_hilt_compiler = \"agndroidx.hilt:hilt-compiler:${version}"
}

object AndroidX {
    const val annotation = "androidx.annotation:annotation:1.2.0-alpha01"

    private const val activityVersion = "1.3.1"
    const val activity = "androidx.activity:activity:$activityVersion"
    const val activity_ktx = "androidx.activity:activity-ktx:$activityVersion"

    const val appcompat = "androidx.appcompat:appcompat:1.4.0-alpha03"
    const val core_ktx = "androidx.core:core-ktx:1.7.0-alpha01"

    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:2.4.0-alpha09"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:2.4.0-alpha09"

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
    private const val compose_version = "1.1.0-alpha03"
    const val compose_ui = "androidx.compose.ui:ui:$compose_version"
    const val compose_material = "androidx.compose.material:material:$compose_version"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:$compose_version"
    const val foundation = "androidx.compose.foundation:foundation:$compose_version"
    const val compose_icon = "androidx.compose.material:material-icons-core:$compose_version"
    const val expand_icon = "androidx.compose.material:material-icons-extended:$compose_version"
    const val runtime_livedata = "androidx.compose.runtime:runtime-livedata:$compose_version"
    const val compose_junit = "androidx.compose.ui:ui-test-junit4:$compose_version"
    const val themeAdapter = "com.google.android.material:compose-theme-adapter:1.0.1"
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

object Billing{
    private const val billing_version = "4.0.0"
    const val billing = "com.android.billingclient:billing:$billing_version"
    const val billing_ktx = "com.android.billingclient:billing-ktx:$billing_version"
}

object Dep2 {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.1"

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        object Activity {
            private const val version = "1.3.1"
            const val activity = "androidx.activity:activity-ktx:$version"
            const val compose = "androidx.activity:activity-compose:$version"
        }
        const val fragment = "androidx.fragment:fragment-ktx:1.3.5"
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val browser = "androidx.browser:browser:1.3.0"

        object Lifecycle {
            private const val lifecycleVersion = "2.3.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        }

        object Navigation {
            private const val version = "2.3.5"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        }
    }

    object Compose {
        const val version = "1.0.1"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"

        const val themeAdapter = "com.google.android.material:compose-theme-adapter:$version"
        const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
    }

    object Kotlin {
        const val version = "1.5.21"
        const val coroutineVersion = "1.5.1"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.2.1"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"

        object Test {
            const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        }
    }

    object Dagger {
        private const val daggerVersion = "2.38.1"
        const val hiltAndroid = "com.google.dagger:hilt-android:$daggerVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$daggerVersion"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerVersion"
    }

    object Accompanist {
        private const val version = "0.16.0"
        const val flowLayout = "com.google.accompanist:accompanist-flowlayout:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pagerIndicators = "com.google.accompanist:accompanist-pager-indicators:$version"
    }

    object Coil {
        private const val version = "1.3.2"
        const val core = "io.coil-kt:coil:$version"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object Square {
        const val okhttp3_logging = "com.squareup.okhttp3:logging-interceptor:4.9.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val junitExt = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:3.2.0"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val inject = "javax.inject:javax.inject:1"
}