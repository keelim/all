#!/usr/bin/env python3
"""Generate the repository module dependency graph via Gradle."""

from __future__ import annotations

import os
from pathlib import Path
import subprocess
import sys


def find_repo_root() -> Path:
    current = Path(__file__).resolve().parent
    for parent in [current, *current.parents]:
        if (parent / "gradlew").is_file() or (parent / "settings.gradle.kts").is_file():
            return parent
    return Path(__file__).resolve().parents[3]


ROOT = find_repo_root()


def main(argv: list[str] | None = None) -> int:
    extra_args = argv if argv is not None else sys.argv[1:]
    gradlew = ROOT / "gradlew"
    if not (gradlew.is_file() and os.access(gradlew, os.X_OK)):
        print('Error: required executable "./gradlew" was not found.', file=sys.stderr)
        return 1

    # Configure the entire project before collecting declared project dependencies.
    # Pass -PmoduleGraph.module=:app-arducon to select its reachable subgraph.
    cmd = [
        str(gradlew),
        "--no-configure-on-demand",
        "generateModuleDependencyGraph",
        *extra_args,
    ]
    proc = subprocess.run(cmd, cwd=ROOT, check=False)
    return proc.returncode


if __name__ == "__main__":
    raise SystemExit(main())
