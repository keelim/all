import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.library")
    id("android-setting-plugin")
    id("kotlin-android")
    id("kotlin-setting-plugin")
}
configure<BaseExtension> {
    buildFeatures.viewBinding = true
}