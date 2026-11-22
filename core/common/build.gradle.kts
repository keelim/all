@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.keelim.multiplatformLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

kotlin {
    android {
        namespace = "com.keelim.commonAndroid"
        androidResources.enable = true
        lint {
            abortOnError = providers.environmentVariable("CI")
                .map { it.equals("true", ignoreCase = true) }
                .orElse(false)
                .get()
        }
        withJava()
        withHostTest {}
    }

    applyDefaultHierarchyTemplate {
        common {
            group("jvmAndAndroid") {
                withJvm()
                withCompilations { it.target is KotlinMultiplatformAndroidLibraryTarget }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.paging.multiplatform)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
        }

        getByName("jvmAndAndroidMain").dependencies {
            implementation(libs.jakarta.inject)
        }

        androidMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.shared)
            implementation(projects.core.dataApi)

            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.startup)
            implementation(libs.androidx.lifecycle.process)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewModel.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.bio)
            implementation(libs.androidx.dataStore.preferences)
            implementation(libs.androidx.work.ktx)
            implementation(libs.fragment.ktx)
            implementation(libs.androidx.hilt.navigation.compose)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.material)
            implementation(libs.androidx.compose.material3)
            implementation(libs.androidx.compose.runtime)
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(project.dependencies.platform(libs.coil.bom))
            implementation(libs.bundles.coil)
            implementation(libs.hilt.android)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.timber)
            implementation(libs.zxing)
        }

        getByName("androidHostTest").dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.runner.junit5)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.mockk.android)
            implementation(libs.turbine)
            runtimeOnly(libs.junit.platform.launcher)
        }
    }
}

dependencies {
    add("kspAndroid", libs.hilt.compiler)
    add("lintChecks", libs.slack.lint.checks)
    add("lintChecks", libs.slack.compose.lint)
}
