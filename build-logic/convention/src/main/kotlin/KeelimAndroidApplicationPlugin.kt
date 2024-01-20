import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import com.android.build.api.dsl.ApplicationExtension
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import com.keelim.builds.configureDependencyGuard
import com.keelim.builds.configureKotlinAndroid
import com.keelim.builds.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KeelimAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
                apply("com.google.android.gms.oss-licenses-plugin")
                apply("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
                apply("com.dropbox.dependency-guard")
                apply("androidx.baselineprofile")
            }


            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                configureDependencyGuard()
                defaultConfig {
                    versionName = libs.findVersion("versionCode").get().displayName
                    versionCode = libs.findVersion("versionCode").get().displayName.toInt()
                    targetSdk = libs.findVersion("targetSdk").get().displayName.toInt()
                }
                with(buildFeatures) {
                    buildConfig = true
                }
                buildTypes.getByName("release").apply {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    configure<BaselineProfileConsumerExtension> {
                        automaticGenerationDuringBuild = true
                    }
                    experimentalProperties["android.experimental.r8.dex-startup-optimization"] = true
                }
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
