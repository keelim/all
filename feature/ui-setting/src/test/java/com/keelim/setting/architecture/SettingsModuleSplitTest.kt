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
    val uiSettingBuild = repoRoot.resolve("feature/ui-setting/build.gradle.kts")

    test("settings gradle includes split settings feature modules") {
        val gradleText = settingsGradle.readText()
        listOf(
            ":feature:settings-core",
            ":feature:settings-theme",
            ":feature:settings-notification",
            ":feature:settings-alarm",
            ":feature:settings-device",
            ":feature:settings-admin",
            ":feature:settings-lab",
            ":core:device-android",
        ).forEach { modulePath ->
            gradleText shouldContain modulePath
        }
    }

    test("ui-setting compatibility module keeps split settings modules internal") {
        val gradleText = uiSettingBuild.readText()
        listOf(
            "implementation(projects.feature.settingsCore)",
            "implementation(projects.feature.settingsTheme)",
            "implementation(projects.feature.settingsNotification)",
            "implementation(projects.feature.settingsAlarm)",
            "implementation(projects.feature.settingsDevice)",
            "implementation(projects.feature.settingsAdmin)",
            "implementation(projects.feature.settingsLab)",
        ).forEach { dependency ->
            gradleText shouldContain dependency
        }
    }

    test("settings feature modules depend on data contracts instead of data implementation") {
        listOf(
            "feature/ui-setting/build.gradle.kts",
            "feature/settings-core/build.gradle.kts",
            "feature/settings-theme/build.gradle.kts",
            "feature/settings-notification/build.gradle.kts",
            "feature/settings-alarm/build.gradle.kts",
            "feature/settings-device/build.gradle.kts",
            "feature/settings-admin/build.gradle.kts",
            "feature/settings-lab/build.gradle.kts",
        ).map(repoRoot::resolve).forEach { buildFile ->
            val gradleText = buildFile.readText()
            check(!gradleText.contains("projects.core.data)")) {
                "${buildFile.fileName} must not depend on core:data implementation"
            }
            check(!gradleText.contains("projects.core.domain")) {
                "${buildFile.fileName} must not depend on core:domain for app-level contracts"
            }
        }
    }

    test("common-android does not depend on upper data network or domain modules") {
        val gradleText = repoRoot.resolve("core/common-android/build.gradle.kts").readText()
        listOf(
            "projects.core.component",
            "projects.core.data)",
            "projects.core.network",
            "projects.core.domain",
            "libs.play.services.ad",
            "libs.play.services.auth",
            "libs.play.services.auth.api.phone",
        ).forEach { forbiddenDependency ->
            check(!gradleText.contains(forbiddenDependency)) {
                "common-android must not depend on $forbiddenDependency"
            }
        }

        sourceFilesUnder(repoRoot.resolve("core/common-android/src/main/java")).forEach { sourceFile ->
            check(!sourceFile.readText().contains("import com.google.android.gms.")) {
                "${repoRoot.relativize(sourceFile)} must keep Google Play Services SDK imports in core:device-android"
            }
        }
    }

    test("device-android owns extracted Android SDK startup helpers") {
        val deviceBuild = repoRoot.resolve("core/device-android/build.gradle.kts")
        deviceBuild.shouldExist()

        val deviceGradleText = deviceBuild.readText()
        listOf(
            "libs.play.services.ad",
            "libs.play.services.auth",
            "libs.play.services.auth.api.phone",
            "libs.zxing",
        ).forEach { expectedDependency ->
            deviceGradleText shouldContain expectedDependency
        }

        listOf(
            "core/device-android/src/main/java/com/keelim/commonAndroid/initialize/MobileAdsInitializer.kt",
            "core/device-android/src/main/java/com/keelim/commonAndroid/receiver/SmsBroadcastReceiver.kt",
            "core/device-android/src/main/java/com/keelim/commonAndroid/ui/sms/SMSRetriever.kt",
            "core/device-android/src/main/java/com/keelim/commonAndroid/model/AppInfo.kt",
            "core/device-android/src/main/java/com/keelim/commonAndroid/util/ApplicationMonitor.kt",
            "core/device-android/src/main/java/com/keelim/commonAndroid/util/AppSignatureHashUtil.kt",
        ).map(repoRoot::resolve).forEach { extractedFile ->
            extractedFile.shouldExist()
        }
    }

    test("common source remains free of Android framework Compose and SDK imports") {
        val forbiddenImports = listOf(
            "import android.",
            "import androidx.activity",
            "import androidx.compose",
            "import androidx.lifecycle",
            "import com.google.zxing",
            "import dagger.",
            "import dagger.hilt",
        )

        sourceFilesUnder(repoRoot.resolve("core/common/src/main/java")).forEach { sourceFile ->
            val source = sourceFile.readText()
            forbiddenImports.forEach { forbiddenImport ->
                check(!source.contains(forbiddenImport)) {
                    "${repoRoot.relativize(sourceFile)} must not contain $forbiddenImport"
                }
            }
        }
    }

    test("ui-setting compatibility module owns the ui-web adapter") {
        val directUiWebDependents = listOf(
            "feature/ui-setting/build.gradle.kts",
        ).map(repoRoot::resolve).toSet()

        listOf(
            "feature/ui-setting/build.gradle.kts",
            "feature/settings-core/build.gradle.kts",
            "feature/settings-theme/build.gradle.kts",
            "feature/settings-notification/build.gradle.kts",
            "feature/settings-alarm/build.gradle.kts",
            "feature/settings-device/build.gradle.kts",
            "feature/settings-admin/build.gradle.kts",
            "feature/settings-lab/build.gradle.kts",
        ).map(repoRoot::resolve).forEach { buildFile ->
            val containsUiWeb = buildFile.readText().contains("projects.feature.uiWeb")
            containsUiWeb shouldBe (buildFile in directUiWebDependents)
        }

        sourceFilesUnder(repoRoot.resolve("feature/settings-core/src/main/java")).forEach { sourceFile ->
            check(!sourceFile.readText().contains("com.keelim.web")) {
                "${repoRoot.relativize(sourceFile)} must use the BrowserLauncher contract instead of feature:ui-web"
            }
        }
    }

    test("app-function aggregate adapter delegates health status payload creation") {
        val appFunctionSource = repoRoot.resolve(
            "feature/app-function/src/main/java/com/keelim/appfunction/health/HealthAppFunctions.kt",
        ).readText()
        val providerSource = repoRoot.resolve(
            "feature/app-function/src/main/java/com/keelim/appfunction/health/HealthStatusProvider.kt",
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
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/settings/SettingsScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/settings/SettingsViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/welcome/WelcomeScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/theme/ThemeScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/theme/ThemeViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/notification/NotificationScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/notification/NotificationViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/alarm/AlarmScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/alarm/AlarmSection.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/alarm/AlarmViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/device/DeviceInfoScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/device/DeviceInfoViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/admin/AdminScreen.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/admin/AdminSection.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/admin/AdminViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/lab/LabRoute.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/lab/LabUiState.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/screen/lab/LabViewModel.kt",
            "feature/ui-setting/src/main/java/com/keelim/setting/di/DeviceInfoModule.kt",
        ).map(repoRoot::resolve).forEach { migratedFile ->
            migratedFile.shouldNotExist()
        }
    }

    test("split settings module directories exist") {
        listOf(
            "feature/settings-core",
            "feature/settings-theme",
            "feature/settings-notification",
            "feature/settings-alarm",
            "feature/settings-device",
            "feature/settings-admin",
            "feature/settings-lab",
        ).map(repoRoot::resolve).forEach { moduleDir ->
            moduleDir.shouldExist()
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
