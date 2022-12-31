import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.keelim.builds.configureAndroidCompose

class KeelimAndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.android.library")
            extensions.getByType<LibraryExtension>()
                .also(::configureAndroidCompose)
        }
    }
}
