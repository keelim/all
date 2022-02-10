@file:Suppress("ClassName")
object Dep {
    object GradlePlugin {
        const val androidStudioGradlePluginVersion = "7.0.4"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.2.0"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
    }

    object AndroidX {
        object activity {
            const val activityVersion = "1.4.0"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        object arch {
            const val testing = "androidx.arch.core:core-testing:2.1.0"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.4.0"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        object fragment {
            private const val fragmentVersion = "1.4.0"
            const val fragment = "androidx.fragment:fragment:$fragmentVersion"
            const val ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"
        }

        object lifecycle {
            const val lifecycleVersion = "2.4.0"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val service = "androidx.lifecycle:lifecycle-service:$lifecycleVersion"
            const val runtimeTesting = "androidx.lifecycle:lifecycle-runtime-testing:$lifecycleVersion"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
            const val livedata  = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val process = "androidx.lifecycle:lifecycle-process:$lifecycleVersion"
        }

        object room {
            private const val roomVersion = "2.4.0-beta02"
            const val runtime = "androidx.room:room-runtime:$roomVersion"
            const val compiler = "androidx.room:room-compiler:$roomVersion"
            const val ktx = "androidx.room:room-ktx:$roomVersion"
            const val testing = "androidx.room:room-testing:$roomVersion"
        }

        object paging{
            const val version = "3.1.0"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        object WorkManager{
            const val version = "2.7.1"
            const val work ="androidx.work:work-runtime-ktx:$version"
        }

        object startup{
            const val version="1.1.0"
            const val runtime = "androidx.startup:startup-runtime:$version"
        }

        object datastore{
            const val version="1.0.0-rc01"
            const val core = "androidx.datastore:datastore-core:$version"
            const val preference = "androidx.datastore:datastore-preferences:$version"
        }

        object navigation {
            const val version = "2.3.5"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        }

        object UI {
            const val browser = "androidx.browser:browser:1.4.0"
            const val material = "com.google.android.material:material:1.5.0"
            const val palette = "androidx.palette:palette:1.0.0"
            const val preference = "androidx.preference:preference-ktx:1.1.1"
            const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
            const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
            const val recycler_selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
            const val viewPager = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
        }

        object Compose {
            const val version = "1.0.5"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val materialAdapter = "com.google.android.material:compose-theme-adapter:${version}"
            const val material3 = "androidx.compose.material3:material3:1.0.0-alpha04"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val animation = "androidx.compose.animation:animation:$version"

            const val activity = "androidx.activity:activity-compose:${AndroidX.activity.activityVersion}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${AndroidX.lifecycle.lifecycleVersion}"
            const val glance = "androidx.glance:glance-appwidget:1.0.0-alpha01"
        }

        object hilt{
            const val version = "1.0.0"
            const val common = "androidx.hilt:hilt-common:$version"
            const val work = "androidx.hilt:hilt-work:$version"
        }

        const val viewBinding =
            "androidx.databinding:viewbinding:${GradlePlugin.androidStudioGradlePluginVersion}"
    }

    object Play{
        const val ad = "com.google.android.gms:play-services-ads:20.5.0"
        const val oss = "com.google.android.gms:play-services-oss-licenses:17.0.0"
        const val play_auth = "com.google.android.gms:play-services-auth:19.2.0"
        const val core = "com.google.android.play:core-ktx:1.8.1"
        const val location = "com.google.android.gms:play-services-location:19.0.1"
        const val play_map = "com.google.android.gms:play-services-maps:17.0.1"
        const val maps_sdk = "com.google.maps.android:maps-ktx:3.0.1"
    }

    object Firebase{
        const val platform = "com.google.firebase:firebase-bom:29.0.3"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object Player{
        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.16.1"
    }

    object Accompanist {
        private const val version = "0.20.2"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
    }

    object Dagger {
        const val version = "2.40.1"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
        }
    }

    object Kotlin {
        const val version = "1.5.31"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.5.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val play = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
    }

    const val jsoup = "org.jsoup:jsoup:1.14.3"

    object Coil {
        private const val version = "1.4.0"
        const val core = "io.coil-kt:coil:$version"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object OkHttp {
        private const val version = "4.9.2"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Network{
        object Retrofit{
            const val rversion = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$rversion"
            const val retrofit_gson = "com.squareup.retrofit2:converter-gson:$rversion"
            const val retrofit_moshi = "com.squareup.retrofit2:converter-moshi:$rversion"
        }
        object Moshi{
            const val moshi = "com.squareup.moshi:moshi:1.12.0"
            const val moshi_kotlin = "com.squareup.moshi:moshi-kotlin:1.12.0"
            const val moshi_codegen = "com.squareup.moshi:moshi-kotlin-codegen:1.12.0"
        }
    }

    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val assertJ = "org.assertj:assertj-core:3.21.0"
        const val mockito = "org.mockito:mockito-core:3.12.4"
        const val androidJunit = "androidx.test.ext:junit:1.1.3"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
    }

    object other{
        const val math = "org.apache.commons:commons-math3:3.6.1"
    }

    object AppCenter {
        private const val appCenterSdkVersion = "4.3.1"
        const val analytics = "com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}"
        const val crashes = "com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}"
    }
}