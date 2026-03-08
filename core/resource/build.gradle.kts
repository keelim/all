plugins {
    alias(libs.plugins.keelim.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.keelim.core.resource"
        androidResources.enable = true
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ALL"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.jetbrains.compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.keelim.core.resource"
}
