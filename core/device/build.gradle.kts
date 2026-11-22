plugins {
    alias(libs.plugins.keelim.multiplatform)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.keelim.deviceAndroid"
        withJava()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(projects.core.common)

            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.process)
            implementation(libs.androidx.startup)
            implementation(libs.hilt.android)
            implementation(libs.jakarta.inject)
            implementation(libs.play.services.ad)
            implementation(libs.play.services.auth)
            implementation(libs.play.services.auth.api.phone)
            implementation(libs.timber)
            implementation(libs.zxing)
        }
    }
}

dependencies {
    add("kspAndroid", libs.hilt.compiler)
}
