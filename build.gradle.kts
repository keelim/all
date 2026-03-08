import groovy.json.JsonOutput
import kotlin.math.roundToInt
import org.gradle.api.GradleException
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

data class CoverageModule(
    val modulePath: String,
    val status: String,
    val reason: String,
    val reportTask: String? = null,
    val xmlReport: String? = null,
    val htmlReport: String? = null,
)

data class CoverageMetrics(
    val coveredLines: Int,
    val missedLines: Int,
) {
    val totalLines: Int
        get() = coveredLines + missedLines

    val lineCoveragePercent: Double
        get() = if (totalLines == 0) {
            0.0
        } else {
            ((coveredLines * 10000.0) / totalLines).roundToInt() / 100.0
        }
}

data class CoverageModuleSummary(
    val modulePath: String,
    val status: String,
    val reason: String,
    val reportTask: String?,
    val xmlReport: String?,
    val htmlReport: String?,
    val metrics: CoverageMetrics?,
)

object CoverageSupport {
    fun toManifestEntry(module: CoverageModule): Map<String, Any?> = mapOf(
        "path" to module.modulePath,
        "status" to module.status,
        "reason" to module.reason,
        "reportTask" to module.reportTask,
        "xmlReport" to module.xmlReport,
        "htmlReport" to module.htmlReport,
    )

    fun toJsonMap(metrics: CoverageMetrics): Map<String, Any> = mapOf(
        "coveredLines" to metrics.coveredLines,
        "missedLines" to metrics.missedLines,
        "totalLines" to metrics.totalLines,
        "lineCoveragePercent" to metrics.lineCoveragePercent,
    )

    fun toJsonMap(summary: CoverageModuleSummary): Map<String, Any?> = buildMap {
        put("path", summary.modulePath)
        put("status", summary.status)
        put("reason", summary.reason)
        put("reportTask", summary.reportTask)
        put("xmlReport", summary.xmlReport)
        put("htmlReport", summary.htmlReport)
        put("reportAvailable", summary.metrics != null)
        summary.metrics?.let {
            putAll(toJsonMap(it))
        }
    }

    private fun rootLineCounter(reportFile: File): Element? {
        val factory = DocumentBuilderFactory.newInstance().apply {
            setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
            setFeature("http://xml.org/sax/features/external-general-entities", false)
            setFeature("http://xml.org/sax/features/external-parameter-entities", false)
            isValidating = false
        }
        val builder = factory.newDocumentBuilder().apply {
            setEntityResolver { _, _ -> InputSource(StringReader("")) }
        }
        val document = builder.parse(reportFile)
        val root = document.documentElement

        return (0 until root.childNodes.length)
            .map { root.childNodes.item(it) }
            .filterIsInstance<Element>()
            .firstOrNull { child ->
                child.tagName == "counter" && child.getAttribute("type") == "LINE"
            }
    }

    private fun readLineCoverage(reportFile: File): CoverageMetrics {
        val lineCounter = rootLineCounter(reportFile)
            ?: return CoverageMetrics(
                coveredLines = 0,
                missedLines = 0,
            )
        return CoverageMetrics(
            coveredLines = lineCounter.getAttribute("covered").toInt(),
            missedLines = lineCounter.getAttribute("missed").toInt(),
        )
    }

    fun summarizeCoverage(rootDir: File, modules: List<CoverageModule>): List<CoverageModuleSummary> = modules.map { module ->
        val xmlFile = module.xmlReport?.let { File(rootDir, it) }
        CoverageModuleSummary(
            modulePath = module.modulePath,
            status = module.status,
            reason = module.reason,
            reportTask = module.reportTask,
            xmlReport = module.xmlReport,
            htmlReport = module.htmlReport,
            metrics = if (xmlFile?.exists() == true) readLineCoverage(xmlFile) else null,
        )
    }

    fun aggregateCoverage(summaries: List<CoverageModuleSummary>): CoverageMetrics {
        val coveredLines = summaries.mapNotNull { it.metrics?.coveredLines }.sum()
        val missedLines = summaries.mapNotNull { it.metrics?.missedLines }.sum()
        return CoverageMetrics(
            coveredLines = coveredLines,
            missedLines = missedLines,
        )
    }

    fun writeJson(outputFile: File, payload: Any) {
        outputFile.parentFile.mkdirs()
        outputFile.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(payload)))
    }
}

object CoveragePlan {
    const val thresholdPercent = 95.0
    const val metric = "line"
    const val policyVersion = "2026-03-08"
    const val manifestOutputPath = "reports/coverage/coverage-manifest.json"
    const val trustedParticipantsSummaryOutputPath = "reports/coverage/trusted-participants-summary.json"
    const val repoObservationSummaryOutputPath = "reports/coverage/repo-observation-summary.json"
    val canonicalExclusions = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
    )

    val trustedParticipantModules = listOf(
        CoverageModule(
            modulePath = ":feature:app-function",
            status = "trusted participant",
            reason = "Verified in the approved execution wave: emits debug XML/HTML coverage and assembles debug cleanly.",
            reportTask = ":feature:app-function:jacocoTestDebugUnitTestReport",
            xmlReport = "feature/app-function/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "feature/app-function/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
    )

    val blockedCandidateModules = listOf(
        CoverageModule(
            modulePath = ":app-my-grade",
            status = "candidate blocked on named trust issue",
            reason = "Still awaiting explicit trusted-participant promotion after the baseline trust gate review.",
            reportTask = ":app-my-grade:jacocoTestDebugUnitTestReport",
            xmlReport = "app-my-grade/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "app-my-grade/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
        CoverageModule(
            modulePath = ":app-arducon",
            status = "candidate blocked on named trust issue",
            reason = "JaCoCo wiring now limits report tasks to real unit-test variants, but the module still awaits explicit trusted-participant promotion after baseline review.",
            reportTask = ":app-arducon:jacocoTestDebugUnitTestReport",
            xmlReport = "app-arducon/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "app-arducon/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
        CoverageModule(
            modulePath = ":app-comssa",
            status = "candidate blocked on named trust issue",
            reason = "JaCoCo wiring now uses the scoped AGP class artifacts, but the module still awaits explicit trusted-participant promotion after baseline review.",
            reportTask = ":app-comssa:jacocoTestDebugUnitTestReport",
            xmlReport = "app-comssa/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "app-comssa/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
        CoverageModule(
            modulePath = ":app-nanda",
            status = "candidate blocked on named trust issue",
            reason = "Unit-test dependency parity is incomplete, so test compilation is not yet trustworthy.",
        ),
        CoverageModule(
            modulePath = ":core:data",
            status = "candidate blocked on named trust issue",
            reason = "Coverage output exists, but explicit trusted-participant promotion is still pending the baseline trust review.",
            reportTask = ":core:data:jacocoTestDebugUnitTestReport",
            xmlReport = "core/data/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "core/data/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
        CoverageModule(
            modulePath = ":core:common-android",
            status = "candidate blocked on named trust issue",
            reason = "Android-library JaCoCo participation now emits XML/HTML, but explicit trusted-participant promotion is still pending the baseline trust review.",
            reportTask = ":core:common-android:jacocoTestDebugUnitTestReport",
            xmlReport = "core/common-android/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "core/common-android/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
        CoverageModule(
            modulePath = ":core:database",
            status = "candidate blocked on named trust issue",
            reason = "The module still needs an explicit trust decision because visible coverage is androidTest-only today.",
        ),
        CoverageModule(
            modulePath = ":core:network",
            status = "candidate blocked on named trust issue",
            reason = "The module remains outside the trusted set until a fresh trust review confirms its report path and test slice.",
        ),
        CoverageModule(
            modulePath = ":core:domain",
            status = "candidate blocked on named trust issue",
            reason = "JVM JaCoCo wiring now emits XML/HTML, but the module still awaits explicit trusted-participant promotion after baseline review.",
            reportTask = ":core:domain:jacocoTestReport",
            xmlReport = "core/domain/build/reports/jacoco/test/jacocoTestReport.xml",
            htmlReport = "core/domain/build/reports/jacoco/test/html/index.html",
        ),
    )

    val observationalModules = listOf(
        CoverageModule(
            modulePath = ":app-cnubus",
            status = "observational / out of scope by policy",
            reason = "JaCoCo stays observational only because the module currently has no trusted unit-test slice.",
        ),
        CoverageModule(
            modulePath = ":core:testing",
            status = "observational / out of scope by policy",
            reason = "Support-only test utilities stay non-gating while the trusted-participants baseline is established.",
        ),
        CoverageModule(
            modulePath = ":feature:ui-setting",
            status = "observational / out of scope by policy",
            reason = "Coverage remains visible but non-gating until the business-logic trusted set is stabilized.",
            reportTask = ":feature:ui-setting:jacocoTestDebugUnitTestReport",
            xmlReport = "feature/ui-setting/build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml",
            htmlReport = "feature/ui-setting/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html",
        ),
    )

    val repoObservationModules = buildList {
        addAll(trustedParticipantModules)
        addAll(
            blockedCandidateModules.filter { blockedCandidate ->
                blockedCandidate.modulePath in setOf(
                    ":app-my-grade",
                    ":app-arducon",
                    ":app-comssa",
                    ":core:data",
                    ":core:common-android",
                    ":core:domain",
                )
            }
        )
        addAll(
            observationalModules.filter { observationalModule ->
                observationalModule.reportTask != null
            }
        )
    }

    fun manifestPayload(): Map<String, Any?> = mapOf(
        "policyVersion" to policyVersion,
        "metric" to metric,
        "thresholdPercent" to thresholdPercent,
        "repoWideCoveragePolicy" to "observational only",
        "rootTasks" to mapOf(
            "trustedParticipantsReport" to "coverageTrustedParticipantsReport",
            "trustedParticipantsVerify" to "coverageTrustedParticipantsVerify",
            "scopedReportAlias" to "coverageScopedReport",
            "scopedVerifyAlias" to "coverageScopedVerify",
            "repoObservation" to "coverageRepoObservation",
        ),
        "canonicalExclusions" to canonicalExclusions,
        "trustedParticipants" to trustedParticipantModules.map(CoverageSupport::toManifestEntry),
        "candidatesNotYetTrusted" to blockedCandidateModules.map(CoverageSupport::toManifestEntry),
        "observationalModules" to observationalModules.map(CoverageSupport::toManifestEntry),
    )
}

buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(libs.play.services.oss.plugin) {
            exclude(group = "com.google.protobuf")
        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.cacheFixPlugin) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.hot.reload) apply false
    alias(libs.plugins.dependency.analysis) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.moduleGraphAssertion) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.secrets) apply false
}

tasks.register("coverageManifest") {
    group = "verification"
    description = "Publishes the trusted-participants coverage manifest for the approved scoped coverage program."
    val manifestOutputFile = layout.buildDirectory.file(CoveragePlan.manifestOutputPath)

    doLast {
        CoverageSupport.writeJson(
            outputFile = manifestOutputFile.get().asFile,
            payload = CoveragePlan.manifestPayload(),
        )
    }
}

tasks.register("coverageTrustedParticipantsReport") {
    group = "verification"
    description = "Generates machine-readable line-coverage output for trusted participants only."
    dependsOn("coverageManifest")
    dependsOn(CoveragePlan.trustedParticipantModules.mapNotNull(CoverageModule::reportTask))
    val rootDir = layout.projectDirectory.asFile
    val trustedParticipantsOutputFile = layout.buildDirectory.file(CoveragePlan.trustedParticipantsSummaryOutputPath)

    doLast {
        val summaries = CoverageSupport.summarizeCoverage(
            rootDir = rootDir,
            modules = CoveragePlan.trustedParticipantModules,
        )
        val aggregate = CoverageSupport.aggregateCoverage(summaries)
        CoverageSupport.writeJson(
            outputFile = trustedParticipantsOutputFile.get().asFile,
            payload = mapOf(
                "generatedAt" to java.time.Instant.now().toString(),
                "metric" to CoveragePlan.metric,
                "thresholdPercent" to CoveragePlan.thresholdPercent,
                "aggregate" to CoverageSupport.toJsonMap(aggregate),
                "modules" to summaries.map(CoverageSupport::toJsonMap),
                "manifest" to "build/reports/coverage/coverage-manifest.json",
            ),
        )
    }
}

tasks.register("coverageTrustedParticipantsVerify") {
    group = "verification"
    description = "Fails when trusted participants fall below the scoped 95% line-coverage target or lose report output."
    dependsOn("coverageTrustedParticipantsReport")
    val rootDir = layout.projectDirectory.asFile

    doLast {
        val summaries = CoverageSupport.summarizeCoverage(
            rootDir = rootDir,
            modules = CoveragePlan.trustedParticipantModules,
        )
        val missingReports = summaries.filter { it.metrics == null }
        if (missingReports.isNotEmpty()) {
            throw GradleException(
                "Missing trusted-participant coverage reports: ${missingReports.joinToString { it.modulePath }}"
            )
        }

        val aggregate = CoverageSupport.aggregateCoverage(summaries)
        if (aggregate.totalLines == 0) {
            throw GradleException("Trusted-participant coverage summary has zero executable lines.")
        }

        if (aggregate.lineCoveragePercent < CoveragePlan.thresholdPercent) {
            throw GradleException(
                "Trusted-participant line coverage ${aggregate.lineCoveragePercent}% is below ${CoveragePlan.thresholdPercent}%."
            )
        }
    }
}

tasks.register("coverageScopedReport") {
    group = "verification"
    description = "Alias for coverageTrustedParticipantsReport to preserve the approved coverageScoped* task contract."
    dependsOn("coverageTrustedParticipantsReport")
}

tasks.register("coverageScopedVerify") {
    group = "verification"
    description = "Alias for coverageTrustedParticipantsVerify to preserve the approved coverageScoped* task contract."
    dependsOn("coverageTrustedParticipantsVerify")
}

tasks.register("coverageRepoObservation") {
    group = "verification"
    description = "Publishes observational repo-wide line-coverage output without turning it into a merge gate."
    dependsOn("coverageManifest")
    dependsOn(CoveragePlan.repoObservationModules.mapNotNull(CoverageModule::reportTask))
    val rootDir = layout.projectDirectory.asFile
    val repoObservationOutputFile = layout.buildDirectory.file(CoveragePlan.repoObservationSummaryOutputPath)

    doLast {
        val summaries = CoverageSupport.summarizeCoverage(
            rootDir = rootDir,
            modules = CoveragePlan.repoObservationModules,
        )
        val aggregate = CoverageSupport.aggregateCoverage(summaries)
        CoverageSupport.writeJson(
            outputFile = repoObservationOutputFile.get().asFile,
            payload = mapOf(
                "generatedAt" to java.time.Instant.now().toString(),
                "metric" to CoveragePlan.metric,
                "policy" to "observational only",
                "aggregate" to CoverageSupport.toJsonMap(aggregate),
                "modules" to summaries.map(CoverageSupport::toJsonMap),
                "manifest" to "build/reports/coverage/coverage-manifest.json",
            ),
        )
    }
}
