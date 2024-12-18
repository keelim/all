plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.hilt)
    kotlin("plugin.parcelize")
}

android{
    namespace = "com.keelim.labs"
}

