import groovy.json.JsonOutput
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

// Only plain values enter the task: never resolve artifacts or retain Project objects.
@CacheableTask
abstract class GenerateModuleDependencyGraph : DefaultTask() {
    @get:Input abstract val modulePaths: ListProperty<String>
    @get:Input abstract val dependencyRecords: ListProperty<String>
    @get:Input abstract val selectedModule: Property<String>
    @get:OutputDirectory abstract val reportDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val records = dependencyRecords.get().map { it.split('\t') }
        val allNodes = (modulePaths.get() + records.flatMap { it.take(2) }).toSortedSet()
        val allEdges = records.map { it[0] to it[1] }.distinct().sortedWith(
            compareBy<Pair<String, String>> { it.first }.thenBy { it.second },
        )
        val adjacency = allEdges.groupBy({ it.first }, { it.second })
        val filter = selectedModule.get()
        if (filter.isNotEmpty() && filter !in allNodes) {
            throw GradleException("Unknown module '$filter'. Available: ${allNodes.joinToString()}")
        }
        val nodes = if (filter.isEmpty()) allNodes else sortedSetOf<String>().also { reachable ->
            fun visit(node: String) {
                if (reachable.add(node)) adjacency[node].orEmpty().forEach(::visit)
            }
            visit(filter)
        }
        val edges = allEdges.filter { it.first in nodes && it.second in nodes }
        val states = mutableMapOf<String, Int>()
        val stack = mutableListOf<String>()
        val cycles = mutableListOf<List<String>>()
        fun inspect(node: String) {
            when (states[node]) {
                1 -> {
                    cycles += stack.drop(stack.indexOf(node)) + node
                    return
                }
                2 -> return
            }
            states[node] = 1
            stack += node
            adjacency[node].orEmpty().forEach(::inspect)
            stack.removeAt(stack.lastIndex)
            states[node] = 2
        }
        nodes.forEach(::inspect)
        val paths = mutableMapOf<String, List<String>>()
        fun longestFrom(node: String): List<String> = paths.getOrPut(node) {
            val longestChild = adjacency[node].orEmpty().map(::longestFrom)
                .maxByOrNull { it.size }.orEmpty()
            listOf(node) + longestChild
        }
        val longestPath = if (cycles.isEmpty()) nodes.map(::longestFrom)
            .maxByOrNull { it.size }.orEmpty() else emptyList()
        val height = if (cycles.isEmpty()) (longestPath.size - 1).coerceAtLeast(0) else null
        val longestEdges = longestPath.zipWithNext().toSet()
        val identifiers = nodes.withIndex().associate { it.value to "n${it.index}" }
        val graph = buildString {
            appendLine("flowchart TD")
            nodes.forEach { node ->
                val label = node.replace("&", "&amp;").replace("\"", "&quot;")
                    .replace("<", "&lt;").replace(">", "&gt;")
                appendLine("    ${identifiers.getValue(node)}[\"$label\"]")
            }
            edges.forEach { (from, to) ->
                appendLine("    ${identifiers.getValue(from)} --> ${identifiers.getValue(to)}")
            }
            val highlighted = edges.withIndex().filter { it.value in longestEdges }.map { it.index }
            if (highlighted.isNotEmpty()) {
                appendLine("    linkStyle ${highlighted.joinToString(",")} stroke:#d33,stroke-width:3px")
            }
        }
        val scope = "api, implementation, and non-test *MainApi/*MainImplementation"
        val statistics = buildString {
            appendLine("Modules: ${nodes.size}")
            appendLine("Edges: ${edges.size}")
            appendLine("Height (edges): ${height ?: "undefined (cycle detected)"}")
            appendLine("Longest path: ${longestPath.joinToString(" -> ").ifEmpty { "unavailable" }}")
            appendLine("Configurations: $scope")
            appendLine("Cycles: ${cycles.size}")
            cycles.forEach { appendLine(it.joinToString(" -> ")) }
        }
        val payload = linkedMapOf<String, Any?>(
            "schemaVersion" to 1,
            "selectedModule" to filter.ifEmpty { null },
            "configurationScope" to scope,
            "modules" to nodes.toList(),
            "edges" to edges.map { (from, to) ->
                linkedMapOf("from" to from, "to" to to, "configurations" to records
                    .filter { it[0] == from && it[1] == to }.map { it[2] }.distinct().sorted())
            },
            "statistics" to linkedMapOf("moduleCount" to nodes.size, "edgeCount" to edges.size,
                "height" to height, "longestPath" to longestPath, "cycles" to cycles),
        )
        val directory = reportDirectory.get().asFile.apply { mkdirs() }
        directory.resolve("modules.json").writeText(JsonOutput.prettyPrint(JsonOutput.toJson(payload)) + "\n")
        directory.resolve("statistics.txt").writeText(statistics)
        directory.resolve("modules.md").writeText(
            "# Module dependencies\n\n```text\n$statistics```\n\n```mermaid\n$graph```\n",
        )
        logger.lifecycle("{}\nReports: {}", statistics.trimEnd(), directory)
    }
}

val graphReport = tasks.register<GenerateModuleDependencyGraph>("generateModuleDependencyGraph") {
    group = "reporting"
    description = "Reports declared project dependencies as Mermaid, JSON, and text without external renderers."
    selectedModule.convention(providers.gradleProperty("moduleGraph.module").orElse(""))
    reportDirectory.convention(layout.buildDirectory.dir("reports/module-dependencies"))
}

// Evaluate every module first, including convention plugins and KMP source-set configurations.
// Configuration-on-demand is disabled by the shell wrapper so this snapshot is complete.
gradle.projectsEvaluated {
    val projects = rootProject.allprojects.sortedBy { it.path }
    val records = projects.flatMap { owner ->
        owner.configurations.filter { configuration ->
            val name = configuration.name
            name == "api" || name == "implementation" ||
                (!name.contains("test", ignoreCase = true) &&
                    (name.endsWith("MainApi") || name.endsWith("MainImplementation")))
        }.flatMap { configuration ->
            configuration.dependencies.withType(ProjectDependency::class.java).map { dependency ->
                "${owner.path}\t${dependency.path}\t${configuration.name}"
            }
        }
    }.distinct().sorted()
    graphReport.configure {
        modulePaths.set(projects.filter { it != rootProject }.map { it.path })
        dependencyRecords.set(records)
    }
}
