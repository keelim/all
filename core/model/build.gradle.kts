plugins {
    alias(libs.plugins.keelim.jvm.library)
    kotlin("plugin.serialization")
}

dependencies {
    api(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}
