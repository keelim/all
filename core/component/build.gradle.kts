@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.keelim.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.keelim.core.component"
    }
    sourceSets {
        androidMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(libs.accompanist.permissions)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.emoji2.emojipicker)
            implementation(libs.androidx.hilt.navigation.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.inapp.update)
            implementation(libs.material.themAdapter)
            implementation(libs.bundles.compose)
            implementation(libs.bundles.coil)
            implementation(libs.androidx.media.compose)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.androidx.media.exoplayer)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
        }
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.coil.bom))
        }
    }
}
