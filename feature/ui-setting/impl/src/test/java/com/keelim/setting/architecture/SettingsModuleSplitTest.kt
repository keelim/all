package com.keelim.setting.architecture

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.paths.shouldExist
import io.kotest.matchers.paths.shouldNotExist
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.shouldBe
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.readText

class SettingsModuleSplitTest : FunSpec({
    val repoRoot = locateRepoRoot()
    val settingsGradle = repoRoot.resolve("settings.gradle.kts")
    val settingsFeatures = listOf(
        "settings-core" to "settingsCore",
        "settings-theme" to "settingsTheme",
        "settings-notification" to "settingsNotification",
        "settings-alarm" to "settingsAlarm",
        "settings-device" to "settingsDevice",
        "settings-admin" to "settingsAdmin",
        "settings-lab" to "settingsLab",
    )
    val settingsModules = settingsFeatures.map { it.first } + "ui-setting"

    test("settings gradle includes each feature contract and implementation") {
        val gradleText = settingsGradle.readText()
        for (feature in settingsModules) {
            for (layer in listOf("api", "impl")) {
                gradleText shouldContain "\":feature:$feature:$layer\""
                repoRoot.resolve("feature/$feature/$layer/build.gradle.kts").shouldExist()
            }
            val apiBuild = repoRoot.resolve("feature/$feature/api/build.gradle.kts").readText()
            check(!apiBuild.contains(".impl)")) {
                "The $feature API must not depend on feature implementations"
            }
        }
        gradleText shouldContain "\":core:device\""
    }

    test("ui-setting composition keeps settings implementations internal") {
        val gradleText = repoRoot.resolve("feature/ui-setting/impl/build.gradle.kts").readText()
        settingsFeatures.forEach { (_, accessor) ->
            gradleText shouldContain "implementation(projects.feature.$accessor.impl)"
        }
    }

    test("settings feature modules depend on data contracts instead of data implementation") {
        settingsModules.forEach { feature ->
            val buildFile = repoRoot.resolve("feature/$feature/impl/build.gradle.kts")
            val gradleText = buildFile.readText()
            check(!gradleText.contains("projects.core.data.dataImpl")) {
                "$feature must not depend on the data implementation"
            }
            check(!gradleText.contains("projects.core.domain")) {
                "$feature must not depend on core:domain for app-level contracts"
            }
        }
        val settingsPlugin = repoRoot.resolve(
            "build-logic/convention/src/main/kotlin/KeelimSettingsFeaturePlugin.kt",
        ).readText()
        settingsPlugin shouldContain ":core:data:data-api"
        check(!settingsPlugin.contains(":core:data:data-impl"))
    }

    test("common does not depend on upper data network or domain modules") {
        val gradleText = repoRoot.resolve("core/common/build.gradle.kts").readText()
        listOf(
            "projects.core.component",
            "projects.core.data.dataImpl",
            "projects.core.network",
            "projects.core.domain",
            "libs.play.services.ad",
            "libs.play.services.auth",
            "libs.play.services.auth.api.phone",
        ).forEach { forbiddenDependency ->
            check(!gradleText.contains(forbiddenDependency)) {
                "core:common must not depend on $forbiddenDependency"
            }
        }

        sourceFilesUnder(repoRoot.resolve("core/common/src/androidMain/kotlin")).forEach { sourceFile ->
            check(!sourceFile.readText().contains("import com.google.android.gms.")) {
                "${repoRoot.relativize(sourceFile)} must keep Google Play Services SDK imports in core:device androidMain"
            }
        }
    }

    test("device owns the extracted SDK startup helpers in its Android source set") {
        val deviceModule = repoRoot.resolve("core/device")
        val deviceBuild = deviceModule.resolve("build.gradle.kts")
        deviceBuild.shouldExist()
        deviceModule.resolve("src/androidMain/AndroidManifest.xml").shouldExist()

        val deviceGradleText = deviceBuild.readText()
        listOf(
            "libs.play.services.ad",
            "libs.play.services.auth",
            "libs.play.services.auth.api.phone",
            "libs.zxing",
        ).forEach { expectedDependency ->
            deviceGradleText shouldContain expectedDependency
        }

        val androidSources = deviceModule.resolve("src/androidMain/kotlin/com/keelim/commonAndroid")
        listOf(
            "di/DeviceApplicationModule.kt",
            "initialize/MobileAdsInitializer.kt",
            "receiver/SmsBroadcastReceiver.kt",
            "ui/sms/SMSRetriever.kt",
            "util/ApplicationMonitor.kt",
            "util/AppSignatureHashUtil.kt",
        ).map(androidSources::resolve).forEach { extractedFile ->
            extractedFile.shouldExist()
        }
        deviceModule.resolve("src/commonMain/kotlin/com/keelim/commonAndroid/model/AppInfo.kt").shouldExist()
    }

    test("shared common source sets remain free of Android framework Compose and SDK imports") {
        val forbiddenImports = listOf(
            "import android.",
            "import androidx.activity",
            "import androidx.compose",
            "import androidx.lifecycle",
            "import com.google.zxing",
            "import dagger.",
            "import dagger.hilt",
        )

        val sharedSources = listOf("commonMain", "jvmAndAndroidMain").flatMap { sourceSet ->
            sourceFilesUnder(repoRoot.resolve("core/common/src/$sourceSet/kotlin"))
        }
        sharedSources.forEach { sourceFile ->
            val source = sourceFile.readText()
            forbiddenImports.forEach { forbiddenImport ->
                check(!source.contains(forbiddenImport)) {
                    "${repoRoot.relativize(sourceFile)} must not contain $forbiddenImport"
                }
            }
        }
    }

    test("ui-setting compatibility module owns the ui-web adapter") {
        settingsModules.forEach { feature ->
            val buildFile = repoRoot.resolve("feature/$feature/impl/build.gradle.kts")
            val containsUiWeb = buildFile.readText().contains("projects.feature.uiWeb")
            containsUiWeb shouldBe (feature == "ui-setting")
        }

        sourceFilesUnder(repoRoot.resolve("feature/settings-core/impl/src/main/java")).forEach { sourceFile ->
            check(!sourceFile.readText().contains("com.keelim.web")) {
                "${repoRoot.relativize(sourceFile)} must use the BrowserLauncher contract instead of feature:ui-web"
            }
        }
    }

    test("app-function aggregate adapter delegates health status payload creation") {
        val appFunctionSource = repoRoot.resolve(
            "feature/app-function/impl/src/main/java/com/keelim/appfunction/health/HealthAppFunctions.kt",
        ).readText()
        val providerSource = repoRoot.resolve(
            "feature/app-function/impl/src/main/java/com/keelim/appfunction/health/HealthStatusProvider.kt",
        ).readText()

        appFunctionSource shouldContain "@AppFunction"
        appFunctionSource shouldContain "HealthStatusProvider"
        check(!appFunctionSource.contains("System.currentTimeMillis")) {
            "AppFunction aggregate adapter must delegate clock and payload construction"
        }
        providerSource shouldContain "HealthStatusClock"
    }

    test("ui-setting no longer owns migrated settings screen implementations") {
        listOf(
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/settings/SettingsScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/settings/SettingsViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/welcome/WelcomeScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/theme/ThemeScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/theme/ThemeViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/notification/NotificationScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/notification/NotificationViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/alarm/AlarmScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/alarm/AlarmSection.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/alarm/AlarmViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/device/DeviceInfoScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/device/DeviceInfoViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/admin/AdminScreen.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/admin/AdminSection.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/admin/AdminViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/lab/LabRoute.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/lab/LabUiState.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/screen/lab/LabViewModel.kt",
            "feature/ui-setting/impl/src/main/java/com/keelim/setting/di/DeviceInfoModule.kt",
        ).map(repoRoot::resolve).forEach { migratedFile ->
            migratedFile.shouldNotExist()
        }
    }
})

private fun locateRepoRoot(): Path {
    var current = Path("").toAbsolutePath().normalize()
    while (!current.resolve("settings.gradle.kts").exists()) {
        check(current.parent != null) { "Could not locate repo root from $current" }
        current = current.parent
    }
    return current
}

private fun sourceFilesUnder(root: Path): List<Path> {
    return Files.walk(root).use { paths ->
        paths
            .filter { Files.isRegularFile(it) && it.extension == "kt" }
            .sorted()
            .toList()
    }
}
