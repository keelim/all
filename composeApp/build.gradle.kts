@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
    alias(libs.plugins.keelim.multiplatform)
}

kotlin {
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.core.component)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        wasmJsMain.dependencies {
            implementation(projects.core.component)
        }
    }
}

// Enable Compose Hot Reload optimization
// https://github.com/JetBrains/compose-hot-reload?tab=readme-ov-file#optimization-enable-optimizenonskippinggroups-not-required
composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

compose.desktop {
    application {
        mainClass = "com.keelim.all.main.kt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.keelim.all"
            packageVersion = "1.0.0"

            macOS {
                dockName = "all"
            }
            windows {
                packageName = "all"
            }
            linux {
                packageName = "all"
            }
        }
    }
}


// From KotlinConf App
// https://github.com/JetBrains/kotlinconf-app/blob/c81492ee57a8da67390d84ad29f41b08128fe0e1/shared/build.gradle.kts#L193
val buildWebApp by tasks.registering(Copy::class) {
    val wasmDist = "wasmJsBrowserDistribution"

    from(tasks.named(wasmDist).get().outputs.files)

    into(layout.buildDirectory.dir("webApp"))

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

android {
    namespace = "com.keelim.all"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
