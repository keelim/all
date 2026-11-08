#!/usr/bin/env python3
"""Build all-rust-lib native artifacts across Android ABI targets."""

from __future__ import annotations

import argparse
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
RUST_DIR = ROOT / "all-rust-lib"
DEFAULT_TARGETS = (
    "aarch64-linux-android",
    "armv7-linux-androideabi",
    "i686-linux-android",
    "x86_64-linux-android",
)


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(
        description="Build all-rust-lib release binaries for Android targets."
    )
    parser.add_argument(
        "--target",
        dest="targets",
        action="append",
        help="Specific Android target triple(s) to build (defaults to all 4 Android ABIs).",
    )
    args = parser.parse_args(argv)

    cargo = shutil.which("cargo")
    if not cargo:
        print("Error: cargo was not found on PATH.", file=sys.stderr)
        return 1

    targets = tuple(args.targets) if args.targets else DEFAULT_TARGETS
    for target in targets:
        proc = subprocess.run(
            [cargo, "build", "--target", target, "--release"],
            cwd=RUST_DIR,
            check=False,
        )
        if proc.returncode != 0:
            return proc.returncode
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
