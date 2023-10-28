import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.keelim.builds.configureKotlinAndroid
import org.gradle.kotlin.dsl.dependencies
import com.keelim.builds.libs

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
            }
            dependencies {
                add("lintChecks", libs.findLibrary("slack-lint-checks").get())
            }
        }
    }
}
