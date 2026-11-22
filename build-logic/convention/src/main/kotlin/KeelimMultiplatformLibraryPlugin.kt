import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import com.keelim.builds.configureJacoco
import com.keelim.builds.configureKotlinCompilerOptions
import com.keelim.builds.configureMultiplatformAndroid
import com.keelim.builds.configureUnitTests
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalWasmDsl::class)
class KeelimMultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(plugin = "com.android.kotlin.multiplatform.library")
            apply(plugin = "com.android.lint")
            apply(plugin = "org.gradle.jacoco")

            val kotlin = extensions.getByType<KotlinMultiplatformExtension>()
            configureKotlinCompilerOptions(kotlin.compilerOptions)
            kotlin.jvmToolchain(17)
            kotlin.jvm("desktop") {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
            kotlin.wasmJs {
                browser()
            }
            (kotlin as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
                configureMultiplatformAndroid(this)
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }

            configureUnitTests()
            configureJacoco(extensions.getByType<KotlinMultiplatformAndroidComponentsExtension>()) {
                val main = kotlin.targets.getByName("android").compilations.getByName("main")
                files(main.allKotlinSourceSets.flatMap { it.kotlin.srcDirs })
            }
        }
    }
}
