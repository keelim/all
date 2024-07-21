plugins {
    alias(libs.plugins.keelim.jvm.library)
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.kotlinx.coroutines.core)
}
