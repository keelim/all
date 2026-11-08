#!/usr/bin/env python3
"""Run gradle-profiler benchmark scenarios."""

from __future__ import annotations

from pathlib import Path
import shutil
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
    profiler = shutil.which("gradle-profiler")
    if not profiler:
        print("Error: gradle-profiler was not found on PATH.", file=sys.stderr)
        return 1

    gradle_home = Path.home() / ".gradle"
    cmd = [
        profiler,
        "--benchmark",
        "--scenario-file",
        "scenarios.txt",
        "--gradle-user-home",
        str(gradle_home),
        "--output-dir",
        "profile",
        *extra_args,
    ]
    proc = subprocess.run(cmd, cwd=ROOT, check=False)
    return proc.returncode


if __name__ == "__main__":
    raise SystemExit(main())
