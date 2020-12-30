object Versions {
    const val appId = "com.keelim.nandadiagnosis"
    const val minSdk = 24
    const val targetSdk = 30
    const val compileSdk = 30
    const val buildTools = "30.0.3"
    const val versionCode = 28
    const val versionName = "1.0.26"
}

object Retrofit {
    private const val version = "2.9.0"
    const val runtime = "com.squareup.retrofit2:retrofit:$version"
    const val serialization =
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    const val gson = "com.squareup.retrofit2:converter-gson:2.9.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.1"

    // UI
    const val insetter = "dev.chrisbanes:insetter-ktx:0.3.1"
    const val lottie = "com.airbnb.android:lottie:3.5.0"
    const val recyclerViewAnimators = "jp.wasabeef:recyclerview-animators:4.0.1"
    const val stfalconImageviewer = "com.github.stfalcon:stfalcon-imageviewer:1.0.1"

    // Utils
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val threetenAbp = "com.jakewharton.threetenabp:threetenabp:1.3.0"

}

object Kotlin {
    private const val version = "1.4.20"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
    const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"
}

object Coroutines {
    private const val version = "1.4.1"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    const val jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.4.2"
}

object Dagger {
    private const val version = "2.30-alpha"
    const val hilt = "com.google.dagger:hilt-android:$version"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$version"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:$version"
    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
}

object Google {
    const val gmsPlugin = "com.google.gms:google-services:4.3.4"
    const val material = "com.google.android.material:material:1.3.0-beta01"
    const val play = "com.google.android.play:core:1.9.0"
    const val ads = "com.google.android.gms:play-services-ads:19.6.0"
    const val guava = "com.google.guava:guava:30.1-jre"
//    const val billing = "com.android.billingclient:billing-ktx:3.0.2"
}

object AndroidX {
    const val activity = "androidx.activity:activity-ktx:1.2.0-beta01"
    const val annot = "androidx.annotation:annotation:1.2.0-alpha01"
    const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
    const val browser = "androidx.browser:browser:1.3.0-rc01"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val core = "androidx.core:core-ktx:1.5.0-alpha05"
    const val drawerlayout = "androidx.drawerlayout:drawerlayout:1.1.1"
    const val fragment = "androidx.fragment:fragment-ktx:1.3.0-beta01"
    const val preference = "androidx.preference:preference-ktx:1.1.1"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0-beta01"
    const val startup = "androidx.startup:startup-runtime:1.0.0"
    const val transition = "androidx.transition:transition:1.4.0-beta01"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.1.0-alpha01"

    object Hilt {
        private const val version = "1.0.0-alpha02"
        const val work = "androidx.hilt:hilt-work:$version"
        const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
        const val compiler = "androidx.hilt:hilt-compiler:$version"
    }

    object WorkManager {
        private const val version = "2.5.0-beta01"
        const val runtime = "androidx.work:work-runtime-ktx:$version"
        const val gcm = "androidx.work:work-gcm:$version"
        const val testing = "androidx.work:work-testing:$version"
    }
}

object Test {
    const val junit = "junit:junit:4.13.1"
    const val runner = "androidx.test:runner:1.1.0"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
}


object Glide {
    private const val version = "4.11.0"
    const val runtime = "com.github.bumptech.glide:glide:$version"
    const val compiler = "com.github.bumptech.glide:compiler:$version"
}

object Firebase {
    const val core = "com.google.firebase:firebase-core"
    const val crashlytics = "com.google.firebase:firebase-crashlytics"
    const val crashlyticsPlugin = "com.google.firebase:firebase-crashlytics-gradle"
    const val messaging = "com.google.firebase:firebase-messaging"
    const val dynamicLinks = "com.google.firebase:firebase-dynamic-links"
    const val ads = "com.google.firebase:firebase-ads"
    const val analytics = "com.google.firebase:firebase-analytics"
}

object Rx{
    const val android= "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val java= "io.reactivex.rxjava2:rxjava:2.2.20"
    const val kotlin= "io.reactivex.rxjava2:rxkotlin:2.4.0"
}

object Lifecycle {
    private const val version = "2.2.0"
    const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
    const val compiler = "androidx.lifecycle:lifecycle-common-java8:$version"
    const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"

}

object Navigation {
    private const val version = "2.3.2"
    const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
    const val ui = "androidx.navigation:navigation-ui-ktx:$version"
    const val dynamicFeaturesFragment = "androidx.navigation:navigation-dynamic-features-fragment:$version"
    const val safeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
}

object Koin {
    const val koin = "org.koin:koin-android:2.1.5"
}

object Room {
    private const val version = "2.3.0-alpha03"
    const val runtime = "androidx.room:room-runtime:$version"
    const val compiler = "androidx.room:room-compiler:$version"
    const val ktx = "androidx.room:room-ktx:$version"
    const val testing = "androidx.room:room-testing:2.2.6"
}
