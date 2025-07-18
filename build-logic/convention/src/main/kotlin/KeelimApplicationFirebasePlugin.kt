
import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class KeelimApplicationFirebasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.gms.google-services")
            apply(plugin = "com.google.firebase.firebase-perf")
            apply(plugin = "com.google.firebase.crashlytics")

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.performance").get())
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
            }

            extensions.configure<ApplicationExtension> {
                buildTypes.configureEach {
                    // Disable the Crashlytics mapping file upload. This feature should only be
                    // enabled if a Firebase backend is available and configured in
                    // google-services.json.
                    buildTypes.getByName("debug" ).apply {
                        configure<CrashlyticsExtension> {
                            mappingFileUploadEnabled = false
                        }
                    }
                    buildTypes.getByName("release" ).apply  {
                        configure<CrashlyticsExtension> {
                            mappingFileUploadEnabled = true
                        }
                    }
                }
            }
        }
    }
}
