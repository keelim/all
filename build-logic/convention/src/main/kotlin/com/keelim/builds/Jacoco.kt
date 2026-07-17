package com.keelim.builds

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.Component
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasHostTests
import com.android.build.api.variant.HostTestBuilder
import com.android.build.api.variant.ScopedArtifacts
import org.gradle.api.file.FileCollection
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale
import java.lang.reflect.Method

private val coverageExclusions = listOf(
    // Android
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

private fun Project.configureJacocoTooling() {
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            // Required for JaCoCo + Robolectric
            // https://github.com/robolectric/robolectric/issues/2230
            // TODO: Consider removing if not we don't add Robolectric
            isIncludeNoLocationClasses = true

            // Required for JDK 11 with the above
            // https://github.com/gradle/gradle/issues/5184#issuecomment-391982009
            excludes = listOf("jdk.internal.*")
        }
    }
}

private fun Project.androidCoverageSourceDirectories() = files(
    "$projectDir/src/main/java",
    "$projectDir/src/main/kotlin",
)

@Suppress("UNCHECKED_CAST")
private fun Project.androidCoverageClassDirectories(variant: Component) = run {
    val scopedArtifacts = unwrapScopedArtifacts(variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT))
    val getFinalArtifacts = scopedArtifacts.javaClass.findDeclaredMethodInHierarchy(
        "getFinalArtifacts",
        ScopedArtifact::class.java,
    ).apply {
        isAccessible = true
    }
    val classDirectories = getFinalArtifacts.invoke(scopedArtifacts, ScopedArtifact.CLASSES) as FileCollection
    classDirectories.asFileTree.matching {
        exclude(coverageExclusions)
    }
}

private fun Class<*>.findDeclaredMethodInHierarchy(
    name: String,
    vararg parameterTypes: Class<*>,
): Method {
    var current: Class<*>? = this
    while (current != null) {
        try {
            return current.getDeclaredMethod(name, *parameterTypes)
        } catch (_: NoSuchMethodException) {
            current.declaredMethods.firstOrNull { method ->
                method.name.startsWith(name) &&
                    method.parameterTypes.contentEquals(parameterTypes)
            }?.let { return it }
            current = current.superclass
        }
    }

    throw NoSuchMethodException("${this.name}.$name(${parameterTypes.joinToString { it.simpleName }})")
}

private fun unwrapScopedArtifacts(scopedArtifacts: Any): Any {
    var current = scopedArtifacts
    while (true) {
        val delegateField = current.javaClass.findDeclaredFieldInHierarchy("delegate") ?: break
        delegateField.isAccessible = true
        val delegate = delegateField.get(current) ?: break
        current = delegate
    }
    return current
}

private fun Class<*>.findDeclaredFieldInHierarchy(name: String) = run {
    var current: Class<*>? = this
    while (current != null) {
        try {
            return@run current.getDeclaredField(name)
        } catch (_: NoSuchFieldException) {
            current = current.superclass
        }
    }
    null
}

internal fun Project.configureJacoco(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    configureJacocoTooling()

    val jacocoTestReport = tasks.register("jacocoTestReport")

    androidComponentsExtension.onVariants { variant ->
        val unitTest = (variant as? HasHostTests)
            ?.hostTests
            ?.get(HostTestBuilder.UNIT_TEST_TYPE)
            ?: return@onVariants
        val testTaskName = "test${unitTest.name.capitalize()}"
        val reportTaskName = "jacoco${testTaskName.capitalize()}Report"
        if (tasks.names.contains(reportTaskName)) {
            return@onVariants
        }

        val reportTask = tasks.register(reportTaskName, JacocoReport::class) {
            dependsOn(testTaskName)

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            classDirectories.setFrom(androidCoverageClassDirectories(variant))
            sourceDirectories.setFrom(androidCoverageSourceDirectories())
            executionData.setFrom(layout.buildDirectory.file("jacoco/$testTaskName.exec"))
        }

        jacocoTestReport.get().dependsOn(reportTask)
    }
}

internal fun Project.configureJvmJacoco() {
    configureJacocoTooling()

    val sourceSets = extensions.getByType<SourceSetContainer>()
    val mainSourceSet = sourceSets.named("main")

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.named("test"))

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        classDirectories.setFrom(
            mainSourceSet.map { sourceSet ->
                sourceSet.output.asFileTree.matching {
                    exclude(coverageExclusions)
                }
            }
        )
        sourceDirectories.setFrom(mainSourceSet.map { it.allSource.srcDirs })
        executionData.setFrom(layout.buildDirectory.file("jacoco/test.exec"))
    }
}
