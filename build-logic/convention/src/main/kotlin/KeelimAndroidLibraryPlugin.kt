
import com.android.build.api.dsl.LibraryExtension
import com.keelim.builds.configureKotlinAndroid
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class KeelimAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "org.gradle.android.cache-fix")
            apply(plugin = "com.jraska.module.graph.assertion")
            apply(plugin = "com.autonomousapps.dependency-analysis")

            extensions.getByType<LibraryExtension>().apply {
                with(buildFeatures) {
                    buildConfig = true
                }
                configureKotlinAndroid(this)
                lint {
                    abortOnError = false
                }
            }
            dependencies {
                add("lintChecks", libs.findLibrary("slack-lint-checks").get())
            }
        }
    }
}
