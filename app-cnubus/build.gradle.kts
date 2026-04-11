import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.application.firebase)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.cnubus"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        // create("cnubus-benchmark") {
        //     signingConfig = signingConfigs.getByName("debug")
        //     matchingFallbacks += listOf("release")
        //     isDebuggable = false
        // }
    }
    namespace = "com.keelim.cnubus"
}

dependencies {

    implementation(projects.core.commonAndroid)
    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.shared)

    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.core.resource)

    implementation(projects.feature.appFunction)
    implementation(projects.feature.uiScheme)
    implementation(projects.feature.uiSetting)
    implementation(projects.widget)


    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.maps.compose.utils)
    implementation(libs.maps.compose.widget)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.deeplinkdispatch)
    ksp(libs.deeplinkdispatch.processor)

    implementation(libs.play.services.oss)

    testImplementation(projects.core.testing)
}

private val viewModelCoverageIncludes = listOf(
    "**/com/keelim/cnubus/ui/screen/**/*ViewModel.class",
    "**/com/keelim/cnubus/ui/screen/**/*ViewModel$*.class",
)

tasks.register<JacocoReport>("jacocoViewModelDebugUnitTestReport") {
    val debugReportTask = tasks.named<JacocoReport>("jacocoTestDebugUnitTestReport")

    dependsOn(debugReportTask)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        debugReportTask.map { reportTask ->
            reportTask.classDirectories.asFileTree.matching {
                include(viewModelCoverageIncludes)
            }
        }
    )
    sourceDirectories.setFrom(
        files(
            "$projectDir/src/main/java",
            "$projectDir/src/main/kotlin",
        )
    )
    executionData.setFrom(debugReportTask.map { it.executionData })
}
