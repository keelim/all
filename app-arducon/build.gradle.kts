import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.keelim.android.application)
    alias(libs.plugins.keelim.android.secrets)
    alias(libs.plugins.keelim.android.application.compose)
    alias(libs.plugins.keelim.android.application.firebase)
    alias(libs.plugins.keelim.android.application.jacoco)
    alias(libs.plugins.keelim.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.keelim.arducon"
    }

    useLibrary("android.test.mock")
    namespace = "com.keelim.arducon"
}

dependencies {

    implementation(projects.core.commonAndroid)
    implementation(projects.core.deviceAndroid)
    implementation(projects.core.common)
    implementation(projects.core.component)
    implementation(projects.core.model)
    implementation(projects.shared)
    implementation(projects.feature.uiWeb)

    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.resource)
    implementation(projects.feature.appFunction)
    implementation(projects.feature.settingsDevice)
    implementation(projects.feature.uiScheme)
    implementation(projects.feature.uiSetting)

    implementation(projects.widget)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.profileinstaller)
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)
    implementation(libs.play.services.ad)
    implementation(libs.play.services.code.scanner)
    implementation(libs.timber)
    implementation(libs.jsoup)
    implementation(libs.tehras.chart)

    testImplementation(projects.core.testing)
}

private val viewModelCoverageIncludes = listOf(
    "**/com/keelim/arducon/ui/screen/**/*ViewModel.class",
    "**/com/keelim/arducon/ui/screen/**/*ViewModel$*.class",
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
