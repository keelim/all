import com.android.build.api.dsl.androidLibrary
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@OptIn(ExperimentalWasmDsl::class)
class KeelimMultiPlatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(plugin = libs.findPlugin("compose-multiplatform").get().get().pluginId)
            apply(plugin = libs.findPlugin("compose-compiler").get().get().pluginId)
            apply(plugin = "com.android.kotlin.multiplatform.library")

            val composeDependencies = extensions.getByType<ComposeExtension>().dependencies
            extensions.configure<KotlinMultiplatformExtension> {
                jvm("desktop")
                androidLibrary {
                    compileSdk = libs.findVersion("compileSdk").get().displayName.toInt()
                    minSdk = libs.findVersion("minSdk").get().displayName.toInt()
                }
                if (project.name.contains("shared").not()) {
                    wasmJs {
                        outputModuleName.set("composeApp")
                        browser {
                            val rootDirPath = project.rootDir.path
                            val projectDirPath = project.projectDir.path

                            commonWebpackConfig {
                                outputFileName = "composeApp.js"
                                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                                    static = (static ?: mutableListOf()).apply {
                                        // Serve sources to debug inside browser
                                        add(rootDirPath)
                                        add(projectDirPath)
                                    }
                                }
                            }

                        }
                        binaries.executable()
                    }
                }
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(composeDependencies.runtime)
                            implementation(composeDependencies.foundation)
                            implementation(composeDependencies.material3)
                            implementation(composeDependencies.materialIconsExtended)
                            implementation(composeDependencies.ui)
                            implementation(composeDependencies.components.resources)
                            implementation(composeDependencies.components.uiToolingPreview)
                        }
                    }
                }
            }
        }
    }
}
