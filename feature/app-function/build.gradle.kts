plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.keelim.appfunction"
}

ksp {
    arg("appfunctions:aggregateAppFunctions", "true")
}

dependencies {
    implementation(projects.core.data)
    implementation(libs.androidx.appfunctions.service)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.appfunctions.compiler)

    testImplementation(projects.core.testing)
}
