import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class KeelimHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dagger.hilt.android.plugin")
            apply(plugin = "com.google.devtools.ksp")

            dependencies {
                "implementation"(libs.findLibrary("hilt.android").get())
                "testImplementation"(libs.findLibrary("hilt.android.testing").get())
                "androidTestImplementation"(libs.findLibrary("hilt.android.testing").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
                "kspTest"(libs.findLibrary("hilt.compiler").get())
                "kspAndroidTest"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
