import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class KeelimSettingsFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "keelim.android.library")
            apply(plugin = "keelim.android.library.compose")
            apply(plugin = "keelim.android.hilt")

            dependencies {
                add("implementation", project(":core:common-android"))
                add("implementation", project(":core:common"))
                add("implementation", project(":core:component"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:resource"))
                add("implementation", project(":shared"))

                add("implementation", project(":core:data-api"))
                add("implementation", project(":core:navigation"))

                add("implementation", libs.findLibrary("androidx.activity.compose").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("timber").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.process").get())

                add("testImplementation", project(":core:testing"))
            }
        }
    }
}
