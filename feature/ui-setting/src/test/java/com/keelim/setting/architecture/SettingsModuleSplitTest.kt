package com.keelim.setting.architecture

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.paths.shouldExist
import io.kotest.matchers.paths.shouldNotExist
import io.kotest.matchers.string.shouldContain
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
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
        ).forEach { modulePath ->
            gradleText shouldContain modulePath
        }
    }

    test("ui-setting compatibility module re-exports split settings feature modules") {
        val gradleText = uiSettingBuild.readText()
        listOf(
            "api(projects.feature.settingsCore)",
            "api(projects.feature.settingsTheme)",
            "api(projects.feature.settingsNotification)",
            "api(projects.feature.settingsAlarm)",
            "api(projects.feature.settingsDevice)",
            "api(projects.feature.settingsAdmin)",
            "api(projects.feature.settingsLab)",
        ).forEach { dependency ->
            gradleText shouldContain dependency
        }
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
