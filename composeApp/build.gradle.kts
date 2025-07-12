plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")
}
