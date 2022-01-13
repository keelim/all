import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("library-setting-plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments.plus(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val version: String = gradleLocalProperties(rootDir).getProperty("VERSION")

    buildTypes {
        defaultConfig{
            buildConfigField("String", "VERSION", version)
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.android)

    implementation(Dep.AndroidX.room.runtime)
    implementation(Dep.AndroidX.paging.common)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)
    implementation(Dep.AndroidX.UI.preference)

    // OkHttp
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    implementation(Dep.Network.Retrofit.retrofit)
    implementation(Dep.Network.Retrofit.retrofit_moshi)
    implementation(Dep.Network.Moshi.moshi_kotlin)
    kapt(Dep.Network.Moshi.moshi_codegen)

    // Dagger Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.AndroidX.room.testing)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Kotlin.coroutines.test)
}

