package com.keelim.setting.architecture

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.paths.shouldExist
import io.kotest.matchers.paths.shouldNotExist
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.shouldBe
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
        ).forEach { forbiddenDependency ->
            check(!gradleText.contains(forbiddenDependency)) {
                "common-android must not depend on $forbiddenDependency"
            }
        }
    }

    test("only settings-core owns the ui-web bridge") {
        val directUiWebDependents = listOf(
            "feature/settings-core/build.gradle.kts",
        ).map(repoRoot::resolve).toSet()

        listOf(
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
