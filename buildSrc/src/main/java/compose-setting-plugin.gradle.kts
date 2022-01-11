import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = Dep.Compose.version
    }
}