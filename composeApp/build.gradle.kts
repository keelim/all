@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
plugins {
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.keelim.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.keelim.all"
    }
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(projects.core.component)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.keelim.all.Main.kt"

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

val checkWasmBundleBudget by tasks.registering {
    group = "verification"
    description = "Checks composeApp and skiko wasm binaries against bundle size budgets."
    dependsOn("wasmJsBrowserDistribution")
    notCompatibleWithConfigurationCache("Reads generated wasm bundle files during execution.")

    doLast {
        val composeAppWasmBudgetBytes = 2_200_000L
        val skikoWasmBudgetBytes = 9_000_000L
        val composeAppWasm = layout.buildDirectory
            .file("compileSync/wasmJs/main/productionExecutable/optimized/composeApp.wasm")
            .get()
            .asFile
        val skikoWasm = layout.buildDirectory
            .file("compose/skiko-runtime-processed-wasmjs/skiko.wasm")
            .get()
            .asFile

        check(composeAppWasm.exists()) {
            "Missing composeApp.wasm bundle at ${composeAppWasm.path}"
        }
        check(skikoWasm.exists()) {
            "Missing skiko.wasm bundle at ${skikoWasm.path}"
        }

        val composeAppWasmSize = composeAppWasm.length()
        val skikoWasmSize = skikoWasm.length()

        check(composeAppWasmSize <= composeAppWasmBudgetBytes) {
            "composeApp.wasm is ${composeAppWasmSize} bytes and exceeds budget ${composeAppWasmBudgetBytes} bytes."
        }
        check(skikoWasmSize <= skikoWasmBudgetBytes) {
            "skiko.wasm is ${skikoWasmSize} bytes and exceeds budget ${skikoWasmBudgetBytes} bytes."
        }
    }
}

buildWebApp.configure {
    dependsOn(checkWasmBundleBudget)
}
