plugins {
    alias(libs.plugins.keelim.jvm.library)
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
