import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Element
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    alias(libs.plugins.keelim.android.library)
    alias(libs.plugins.keelim.android.library.jacoco)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.keelim.appfunction"
    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.unitTests.all {
        // Robolectric's Android 16 shared-memory interceptor accesses SharedSecrets on JDK 17+.
        it.jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED")
    }
}

// AndroidX builds this public wrapper against snapshot-only platform stubs. Publish only
// their compile classpath to consuming apps; the optional system library supplies runtime code.
val extensionStubClasspath = configurations.create("appFunctionExtensionStubClasspath") {
    isCanBeConsumed = false
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
}
val compileExtensionStubs = tasks.register<JavaCompile>("compileAppFunctionExtensionStubs") {
    source(layout.projectDirectory.dir("extension-stubs/src/main/java"))
    classpath = files(androidComponents.sdkComponents.bootClasspath, extensionStubClasspath)
    destinationDirectory.set(layout.buildDirectory.dir("extension-stubs/classes"))
    options.release.set(17)
}
val extensionStubsJar = tasks.register<Jar>("appFunctionExtensionStubsJar") {
    from(compileExtensionStubs.flatMap { it.destinationDirectory })
    archiveClassifier.set("extension-stubs")
    destinationDirectory.set(layout.buildDirectory.dir("extension-stubs/libs"))
}
val extensionStubFiles = files(extensionStubsJar.flatMap { it.archiveFile })

/** Combines compiler-owned indexes while retaining each service's component references. */
abstract class MergeAppFunctionMetadata : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val indexes: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun merge() {
        val factory = DocumentBuilderFactory.newInstance().apply {
            setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "")
            setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "")
        }
        val expectedIds = setOf(
            "com.keelim.appfunction.health.HealthAppFunctions#getHealthStatus",
            "com.keelim.appfunction.json.JsonFormatterAppFunctions#formatJson",
        )
        val destination = outputDirectory.get().asFile.apply { mkdirs() }
        for (legacy in listOf(false, true)) {
            val document = factory.newDocumentBuilder().newDocument()
            val root = document.createElement("appfunctions")
            document.appendChild(root)
            val functionIds = mutableSetOf<String>()
            val componentIds = mutableSetOf<String>()
            val files = indexes.files.filter { it.name.endsWith("-v1.xml") == legacy }
                .sortedBy { it.name }
            check(files.size == 2) { "Expected both compiler-generated AppFunction indexes" }
            for (file in files) {
                val source = factory.newDocumentBuilder().parse(file).documentElement
                check(source.tagName == "appfunctions") { "Unexpected index root: $file" }
                for (index in 0 until source.childNodes.length) {
                    val child = source.childNodes.item(index) as? Element ?: continue
                    when (child.tagName) {
                        "appfunction" -> {
                            val tag = if (legacy) "function_id" else "functionId"
                            val ids = child.getElementsByTagName(tag)
                            check(ids.length == 1) { "Missing or ambiguous function ID: $file" }
                            val id = ids.item(0).textContent
                            check(functionIds.add(id)) { "Duplicate AppFunction ID: $id" }
                        }
                        "AppFunctionComponentMetadataDocument" -> {
                            val ids = (0 until child.childNodes.length).mapNotNull {
                                child.childNodes.item(it) as? Element
                            }.filter { it.tagName == "id" }
                            check(ids.size == 1 && componentIds.add(ids.single().textContent)) {
                                "Missing or duplicate component ID: $file"
                            }
                        }
                        else -> error("Unexpected AppFunction index element: ${child.tagName}")
                    }
                    root.appendChild(document.importNode(child, true))
                }
            }
            check(functionIds == expectedIds) { "AppFunction IDs changed: $functionIds" }
            val name = if (legacy) "keelim_app_functions.xml" else "keelim_app_functions_v2.xml"
            TransformerFactory.newInstance().newTransformer().apply {
                setOutputProperty(OutputKeys.INDENT, "yes")
                setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            }.transform(DOMSource(document), StreamResult(destination.resolve(name)))
        }
    }
}

androidComponents.onVariants { variant ->
    val capitalized = variant.name.replaceFirstChar { it.uppercaseChar() }
    val mergeMetadata = tasks.register<MergeAppFunctionMetadata>("merge${capitalized}AppFunctionMetadata") {
        dependsOn("ksp${capitalized}Kotlin")
        val generated = layout.buildDirectory.dir("generated/ksp/${variant.name}/resources/assets")
        indexes.from(listOf("keelim_health_functions", "keelim_json_functions").flatMap { name ->
            listOf(generated.map { it.file("$name.xml") }, generated.map { it.file("$name-v1.xml") })
        })
        outputDirectory.set(layout.buildDirectory.dir("generated/appfunctionAssets/${variant.name}"))
    }
    variant.sources.assets?.addGeneratedSourceDirectory(mergeMetadata, MergeAppFunctionMetadata::outputDirectory)
}

ksp {
    arg("appfunctions:generateV1Xml", "true")
    arg("appfunctions:generateMetadataFromSchema", "true")
}

dependencies {
    add(extensionStubClasspath.name, libs.androidx.annotation)
    add(extensionStubClasspath.name, libs.jspecify)
    add("compileOnlyApi", extensionStubFiles)
    implementation(projects.core.data)
    implementation(libs.androidx.appfunctions.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    ksp(libs.androidx.appfunctions.compiler)

    testImplementation(projects.core.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk.jvm)
    testImplementation(libs.androidx.appsearch)
    testImplementation(libs.androidx.appsearch.platform)
    testImplementation(libs.guava)
    testRuntimeOnly(extensionStubFiles)
    testRuntimeOnly(libs.junit.vintage.engine)
}
