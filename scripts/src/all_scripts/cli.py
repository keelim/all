"""Central CLI dispatcher for all-scripts tools."""

from __future__ import annotations

import argparse
import sys

from all_scripts import (
    android_light,
    compile_check,
    dependency_tree_update,
    fcm_admin,
    gradle_cache,
    gradle_dependencies_graph,
    gradle_profile,
    hello_world,
    lightbuild_preview,
    rust_build,
)

COMMANDS = {
    "android-light": (android_light.main, "Scoped Android CLI + Gradle Wrapper workflow"),
    "compile-check": (compile_check.main, "Run :compileDebugSources across core apps"),
    "dependency-tree-update": (
        dependency_tree_update.main,
        "Run :dependencyGuardBaseline across core apps",
    ),
    "fcm-admin": (fcm_admin.main, "Send Firebase Cloud Messages to token or topic"),
    "gradle-cache": (gradle_cache.main, "Prune unused Gradle cache files"),
    "gradle-dependencies-graph": (
        gradle_dependencies_graph.main,
        "Generate repository module dependency graph",
    ),
    "gradle-profile": (gradle_profile.main, "Run gradle-profiler benchmark scenarios"),
    "hello-world": (hello_world.main, "Emit escaped single-line payload for CI"),
    "lightbuild-preview": (
        lightbuild_preview.main,
        "Control Google Lightbuild preview project",
    ),
    "rust-build": (rust_build.main, "Build all-rust-lib release binaries for Android ABIs"),
}


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(
        prog="all-scripts",
        description="Unified Python automation tools for the all monorepo.",
    )
    subparsers = parser.add_subparsers(dest="subcommand", help="Available commands")

    for name, (_, help_text) in COMMANDS.items():
        subparsers.add_parser(
            name, help=help_text, add_help=False, prefix_chars=" \t\n\r"
        )

    args, extra = parser.parse_known_args(argv)
    if not args.subcommand:
        parser.print_help()
        return 0

    func, _ = COMMANDS[args.subcommand]
    return func(extra)


if __name__ == "__main__":
    raise SystemExit(main())
