import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.keelim.android.application.room)
    kotlin("plugin.serialization")
    alias(libs.plugins.keelim.multiplatform)
}

kotlin {
    android {
        namespace = "com.keelim.kmp.shared"
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    targets
        .filterIsInstance<KotlinNativeTarget>()
        .forEach { target ->
            target.binaries {
                framework {
                    baseName = "ALL"
                    isStatic = true
                }
            }
        }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.okio)
            implementation(libs.circuit.foundation)

            api(projects.core.resource)
            api(libs.androidx.dataStore.preferences)
            api(libs.androidx.dataStore.core.okio)

            implementation(libs.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.kotlinx.datetime)

            implementation(projects.core.model)
        }

        androidMain.dependencies {
            implementation(projects.core.datastoreProto)
            implementation(libs.androidx.dataStore.core)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
}
