# Module dependency reports

Generate the report from any working directory:

```bash
./script/gradle_dependencies_graph.sh
./script/gradle_dependencies_graph.sh -PmoduleGraph.module=:app-arducon
```

The equivalent Gradle task is `./gradlew --no-configure-on-demand generateModuleDependencyGraph`.
Reports are generated under `build/reports/module-dependencies/`:

- `modules.md`: Mermaid diagram, with the longest dependency path highlighted in red.
- `modules.json`: stable, sorted module paths and edges, including originating configurations.
- `statistics.txt`: module/edge counts, height in edges, longest path, and detected cycles.

The task reads Gradle's configured `ProjectDependency` objects, including declarations
added by convention plugins. It does not parse build scripts, resolve configurations,
download dependency artifacts, or invoke Graphviz, SVG optimizers, or a browser.
Only plain input values are retained by the task, so reports support configuration-cache reuse.

The collected configurations are `api`, `implementation`, and non-test names ending in
`MainApi` or `MainImplementation` (Kotlin Multiplatform production source sets).
Test, compile-only, runtime-only, and Android variant-specific configurations are excluded.
All registered subproject paths appear, including isolated projects and grouping projects
such as `:core`. A selected module reports only that module and its reachable dependencies.
Multiple configuration declarations of the same edge are combined; labels retain full paths.
Composite/included builds, such as `build-logic`, are outside the application graph.

Mermaid renders directly in GitHub Markdown previews. Local Markdown viewers need Mermaid
support; static SVG/PNG rendering is no longer a prerequisite. Split large graphs by app
using `moduleGraph.module` and use JSON/text when a diagram becomes crowded.
Cycles are reported; height and longest path are undefined when a cycle is detected.
The report is observational and does not introduce a new build gate.

The removed `com.jraska.module.graph.assertion` plugin had no configured `allowed`,
`restricted`, `maxHeight`, or `assertOnAnyBuild` rules in this checkout. Its default
`assertModuleGraph` lifecycle task therefore enforced no project-specific restrictions.
This migration removes that empty task; it does not silently remove an active assertion.
Architecture rules in `docs/architecture/module-boundaries.md` remain documentation,
not executable assertions. The old `generateModulesGraphvizText`, statistics tasks,
`modules.graph.*` properties, and module-alias feature are not compatibility interfaces.

The prior tool omitted KMP source-set edges and wrote SVGs beside each module. Existing
tracked SVGs may be historical and are not updated by the new report. Do not use them as
the current dependency source. Project graph removal does not uninstall Homebrew Graphviz.

The dependency-free integration fixture verifies native Gradle collection, source-set and
test filtering, duplicate edges, isolated modules, longest-path output, selected subgraphs,
unknown-module errors, cycle handling, and strict configuration-cache reuse:

```bash
GRADLE_EXECUTABLE=/path/to/existing/gradle python3 script/test_module_dependency_graph.py
```

The fixture runs offline and declares an intentionally unavailable external dependency,
so successful reporting also demonstrates that graph collection does not resolve artifacts.
