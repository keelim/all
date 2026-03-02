plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.keelim.appfunction"
}

ksp {
    arg("appfunctions:aggregateAppFunctions", "true")
}

dependencies {
    implementation(libs.androidx.appfunctions.service)
    ksp(libs.androidx.appfunctions.compiler)

    testImplementation(projects.core.testing)
}
