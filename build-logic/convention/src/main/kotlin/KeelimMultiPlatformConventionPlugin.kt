import com.android.build.api.dsl.androidLibrary
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
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

            val composeRuntime = libs.findLibrary("jetbrains-compose-runtime").get()
            val composeFoundation = libs.findLibrary("jetbrains-compose-foundation").get()
            val composeMaterial3 = libs.findLibrary("jetbrains-compose-material3").get()
            val composeMaterialIconsExtended = libs.findLibrary("jetbrains-compose-materialIconsExtended").get()
            val composeUi = libs.findLibrary("jetbrains-compose-ui").get()
            val composeResources = libs.findLibrary("jetbrains-compose-components-resources").get()
            val composeUiToolingPreview = libs.findLibrary("jetbrains-compose-components-uiToolingPreview").get()

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
                            implementation(composeRuntime)
                            implementation(composeFoundation)
                            implementation(composeMaterial3)
                            implementation(composeMaterialIconsExtended)
                            implementation(composeUi)
                            implementation(composeResources)
                            implementation(composeUiToolingPreview)
                        }
                    }
                }
            }
        }
    }
}
