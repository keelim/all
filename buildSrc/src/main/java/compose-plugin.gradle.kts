import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0"
    }
}
