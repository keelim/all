#!/usr/bin/env python3
"""Prune unused Gradle cache files older than the retention threshold (default: 30 days)."""

from __future__ import annotations

import argparse
import os
from pathlib import Path
import time


def format_bytes(size: int) -> str:
    value = float(size)
    for unit in ("B", "K", "M", "G", "T"):
        if value < 1024.0 or unit == "T":
            return f"{value:.1f}{unit}" if unit != "B" else f"{int(value)}B"
        value /= 1024.0
    return f"{value:.1f}T"


def directory_size(root: Path) -> int:
    total = 0
    if not root.exists():
        return 0
    for dirpath, _, filenames in os.walk(root, followlinks=False):
        for name in filenames:
            filepath = Path(dirpath) / name
            try:
                if not filepath.is_symlink():
                    total += filepath.stat().st_size
            except OSError:
                continue
    return total


def prune_gradle_cache(gradle_dir: Path, days: int, dry_run: bool = False) -> None:
    if not gradle_dir.is_dir():
        print(f"Gradle cache directory does not exist: {gradle_dir}")
        return

    before_size = directory_size(gradle_dir)
    print(f"{format_bytes(before_size)}\t{gradle_dir}")

    cutoff = time.time() - (days * 86400)
    for dirpath, _, filenames in os.walk(
        gradle_dir, topdown=False, followlinks=False
    ):
        current_dir = Path(dirpath)
        for name in filenames:
            filepath = current_dir / name
            try:
                stat_info = filepath.lstat()
                if stat_info.st_atime < cutoff:
                    if not dry_run:
                        filepath.unlink(missing_ok=True)
            except OSError:
                continue

        if current_dir != gradle_dir:
            try:
                if not any(current_dir.iterdir()) and not dry_run:
                    current_dir.rmdir()
            except OSError:
                continue

    after_size = directory_size(gradle_dir)
    print(f"{format_bytes(after_size)}\t{gradle_dir}")


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(
        description="Remove Gradle cache files not accessed within N days."
    )
    parser.add_argument(
        "--gradle-dir",
        type=Path,
        default=Path.home() / ".gradle",
        help="Path to the Gradle user home directory (default: ~/.gradle)",
    )
    parser.add_argument(
        "--days",
        type=int,
        default=30,
        help="Retention threshold in days based on access time (default: 30)",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Inspect sizes without deleting files",
    )
    args = parser.parse_args(argv)
    prune_gradle_cache(args.gradle_dir.expanduser(), args.days, args.dry_run)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
