#!/usr/bin/env python3
"""Run compileDebugSources across Android application modules."""

from __future__ import annotations

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
DEFAULT_MODULES = (
    "app-arducon",
    "app-cnubus",
    "app-comssa",
    "app-my-grade",
    "app-nanda",
)


def main(argv: list[str] | None = None) -> int:
    extra_args = argv if argv is not None else sys.argv[1:]
    gradlew = ROOT / "gradlew"
    if not gradlew.is_file():
        print('Error: required executable "./gradlew" was not found.', file=sys.stderr)
        return 1

    tasks = [f":{module}:compileDebugSources" for module in DEFAULT_MODULES]
    proc = subprocess.run([str(gradlew), *tasks, *extra_args], cwd=ROOT, check=False)
    return proc.returncode


if __name__ == "__main__":
    raise SystemExit(main())
