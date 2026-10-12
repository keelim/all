#!/usr/bin/env python3
"""Dependency-free Gradle integration checks; uses an existing Gradle executable."""
import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import unittest


class ModuleGraphTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.directory = tempfile.TemporaryDirectory(prefix="all-module-graph-")
        cls.root = Path(cls.directory.name)
        cls.gradle = os.environ.get("GRADLE_EXECUTABLE", "gradle")
        if not shutil.which(cls.gradle):
            raise unittest.SkipTest("Set GRADLE_EXECUTABLE to an existing Gradle installation")
        source = Path(__file__).resolve().parents[1] / "gradle/module-dependency-graph.gradle.kts"
        shutil.copyfile(source, cls.root / "graph.gradle.kts")
        (cls.root / "settings.gradle.kts").write_text(
            'rootProject.name = "graph-test"\ninclude(":app", ":core", ":leaf", ":unused")\n'
        )
        (cls.root / "build.gradle.kts").write_text('apply(from = "graph.gradle.kts")\n')
        for name in ("app", "core", "leaf", "unused"):
            (cls.root / name).mkdir()
        (cls.root / "app/build.gradle.kts").write_text('''
plugins { `java-library` }
val commonMainImplementation by configurations.creating
val commonTestImplementation by configurations.creating
val debugImplementation by configurations.creating
dependencies {
    implementation(project(":core"))
    api(project(":core"))
    add(commonMainImplementation.name, project(":leaf"))
    add(commonTestImplementation.name, project(":unused"))
    add(debugImplementation.name, project(":unused"))
    testImplementation(project(":unused"))
    implementation("invalid.example:never-resolve:0")
}
''')
        (cls.root / "core/build.gradle.kts").write_text(
            'plugins { `java-library` }\ndependencies { api(project(":leaf")) }\n'
        )

    @classmethod
    def tearDownClass(cls):
        cls.directory.cleanup()

    def run_graph(self, *arguments, success=True):
        result = subprocess.run(
            [self.gradle, "--offline", "--console=plain", "--configuration-cache",
             "--configuration-cache-problems=fail", "--no-configure-on-demand",
             "generateModuleDependencyGraph", *arguments],
            cwd=self.root, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
        )
        if success:
            self.assertEqual(result.returncode, 0, result.stdout)
        else:
            self.assertNotEqual(result.returncode, 0, result.stdout)
        return result.stdout

    def report(self):
        return json.loads((self.root / "build/reports/module-dependencies/modules.json").read_text())

    def test_01_model_collection_and_cache(self):
        self.run_graph()
        report = self.report()
        self.assertEqual(report["modules"], [":app", ":core", ":leaf", ":unused"])
        self.assertEqual([(e["from"], e["to"]) for e in report["edges"]],
                         [(":app", ":core"), (":app", ":leaf"), (":core", ":leaf")])
        self.assertEqual(report["edges"][0]["configurations"], ["api", "implementation"])
        self.assertEqual(report["statistics"]["longestPath"], [":app", ":core", ":leaf"])
        self.assertEqual(report["statistics"]["height"], 2)
        markdown = (self.root / "build/reports/module-dependencies/modules.md").read_text()
        self.assertIn('n3[":unused"]', markdown)
        self.assertIn("linkStyle 0,2", markdown)
        before = (self.root / "build/reports/module-dependencies/modules.json").read_bytes()
        output = self.run_graph("--rerun-tasks")
        self.assertIn("Reusing configuration cache", output)
        self.assertEqual(before, (self.root / "build/reports/module-dependencies/modules.json").read_bytes())

    def test_02_leaf_and_unknown_module(self):
        self.run_graph("-PmoduleGraph.module=:leaf")
        report = self.report()
        self.assertEqual(report["modules"], [":leaf"])
        self.assertEqual(report["edges"], [])
        self.assertEqual(report["statistics"]["height"], 0)
        output = self.run_graph("-PmoduleGraph.module=:missing", success=False)
        self.assertIn("Unknown module ':missing'", output)

    def test_03_cycle_is_reported_without_recursion_failure(self):
        (self.root / "leaf/build.gradle.kts").write_text(
            'plugins { `java-library` }\ndependencies { api(project(":app")) }\n'
        )
        self.run_graph()
        stats = self.report()["statistics"]
        self.assertIsNone(stats["height"])
        self.assertTrue(stats["cycles"])
        self.assertEqual(stats["longestPath"], [])


if __name__ == "__main__":
    unittest.main()
