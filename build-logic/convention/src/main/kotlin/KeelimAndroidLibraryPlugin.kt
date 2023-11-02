import com.android.build.api.dsl.LibraryExtension
import com.keelim.builds.configureKotlinAndroid
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KeelimAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
            }
            extensions.getByType<LibraryExtension>().apply {
                buildFeatures.dataBinding = true
                configureKotlinAndroid(this)
                lint {
                    abortOnError = false
                }
            }
            dependencies {
                // add("lintChecks", libs.findLibrary("slack-lint-checks").get())
            }
        }
    }
}
