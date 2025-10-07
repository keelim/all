plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.compose)
    alias(libs.plugins.keelim.android.hilt)
    alias(libs.plugins.keelim.android.showkase)
}

android {
    namespace = "com.keelim.compose.core"
    lint {
        disable += "SuspiciousModifierThen"
    }
}

dependencies {

    api(projects.core.component)
}
