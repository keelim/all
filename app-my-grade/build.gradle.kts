import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.secrets)
    alias(libs.plugins.keelim.android.application.firebase)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.mygrade"
        buildTypes {
            // create("my-grade-benchmark") {
            //     signingConfig = signingConfigs.getByName("debug")
            //     matchingFallbacks += listOf("release")
            //     isDebuggable = false
            // }
        }
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.mygrade"
}

dependencies {

    implementation(projects.core.commonAndroid)
    implementation(projects.core.common)
    implementation(projects.core.component)

    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.resource)
    implementation(projects.shared)

    implementation(projects.feature.appFunction)
    implementation(projects.feature.uiSetting)
    implementation(projects.widget)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.apache.math)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.play.services.ad)
    implementation(libs.timber)
    implementation(libs.deeplinkdispatch)
    implementation(libs.firebase.common)
    implementation(platform(libs.firebase.bom))
    ksp(libs.deeplinkdispatch.processor)

    implementation(libs.play.services.oss)

    testImplementation(projects.core.testing)
}

private val viewModelCoverageIncludes = listOf(
    "**/com/keelim/mygrade/ui/screen/**/*ViewModel.class",
    "**/com/keelim/mygrade/ui/screen/**/*ViewModel$*.class",
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
