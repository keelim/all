import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

@Suppress("unused")
class KeelimAndroidSecretsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply(plugin = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    }
}
