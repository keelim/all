#!/usr/bin/env python3
"""Native Google Lightbuild workflow for the independent preview project."""

from __future__ import annotations

import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
import time


def find_repo_root() -> Path:
    current = Path(__file__).resolve().parent
    for parent in [current, *current.parents]:
        if (parent / "gradlew").is_file() or (parent / "settings.gradle.kts").is_file():
            return parent
    return Path(__file__).resolve().parents[3]


ROOT = find_repo_root()
PROJECT_ROOT = ROOT / "previews/lightbuild"
VALID_ACTIONS = {"doctor", "query", "resolve", "build", "test"}


def fail(message: str, code: int = 1) -> int:
    print(message, file=sys.stderr)
    return code


def read_regex(path: Path, pattern: str) -> str | None:
    if not path.is_file():
        return None
    match = re.search(pattern, path.read_text(encoding="utf-8"), re.MULTILINE)
    return match.group(1) if match else None


def main(argv: list[str] | None = None) -> int:
    args = argv if argv is not None else sys.argv[1:]
    if len(args) > 1:
        return fail(
            "Usage: uv run --project scripts lightbuild-preview "
            "{doctor|query|resolve|build|test}",
            2,
        )

    action = args[0] if args else "doctor"
    if action not in VALID_ACTIONS:
        return fail(f"Unknown action: {action}", 2)

    android_bin = shutil.which("android")
    if not android_bin:
        return fail("Android CLI is required on PATH.")

    sdk_proc = subprocess.run(
        [android_bin, "info", "sdk"],
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True,
        check=False,
    )
    sdk_str = sdk_proc.stdout.strip()
    sdk = Path(sdk_str) if sdk_str else Path()

    config_yaml = PROJECT_ROOT / "project.lightbuild.yaml"
    version = read_regex(config_yaml, r"^  version: ([^ \n]+)")
    if not version:
        return fail("Missing pinned Lightbuild version.")

    distribution = sdk / f"build/lightbuild/{version}"
    source_props = distribution / "source.properties"
    daemon = distribution / "lightbuild-daemon"
    if not source_props.is_file() or not (daemon.is_file() and os.access(daemon, os.X_OK)):
        return fail(
            f"Install the official SDK package explicitly: android sdk install build/lightbuild/{version}\n"
            "Review any new license prompt before accepting. This script installs nothing."
        )

    props_content = source_props.read_text(encoding="utf-8")
    if f"Pkg.Revision={version}" not in props_content:
        return fail("The installed package does not match the pinned Lightbuild version.")

    cli = distribution / "cli/lightbuild"
    if not (cli.is_file() and os.access(cli, os.X_OK)):
        return fail("Missing Lightbuild CLI in the pinned SDK package.")

    java_tool = read_regex(ROOT / "mise.toml", r'^java = "([^"]*)"')
    if not java_tool:
        return fail("Missing java tool pin in mise.toml.")

    default_java_home = Path.home() / ".local/share/mise/installs/java" / java_tool
    java_home = Path(os.environ.get("LIGHT_JAVA_HOME") or default_java_home)
    java_bin = java_home / "bin/java"
    if not (java_bin.is_file() and os.access(java_bin, os.X_OK)):
        return fail(f"Missing existing JDK: {java_tool}; set LIGHT_JAVA_HOME.")

    java_proc = subprocess.run(
        [str(java_bin), "-XshowSettings:properties", "-version"],
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
        check=False,
    )
    major_match = re.search(
        r"^\s*java\.specification\.version = (.+)$", java_proc.stdout, re.MULTILINE
    )
    java_major = major_match.group(1).strip() if major_match else ""
    if java_major != "17":
        return fail("The preview requires an installed JDK 17.")

    env = os.environ.copy()
    env["JAVA_HOME"] = str(java_home)
    env["PATH"] = f"{java_home / 'bin'}:{env.get('PATH', '')}"
    env["ANDROID_HOME"] = str(sdk)
    env["ANDROID_SDK_ROOT"] = str(sdk)

    print(f"Independent preview: {PROJECT_ROOT}\nLightbuild SDK package: {version}")

    if action == "doctor":
        res1 = subprocess.run([str(cli), "--version"], cwd=PROJECT_ROOT, env=env, check=False)
        if res1.returncode != 0:
            return res1.returncode
        res2 = subprocess.run([str(java_bin), "-version"], cwd=PROJECT_ROOT, env=env, check=False)
        return res2.returncode

    started = time.monotonic()
    if action == "query":
        cmd = [str(cli), "query", "..."]
    elif action == "resolve":
        cmd = [str(cli), "resolve"]
    elif action == "build":
        cmd = [str(cli), "build", "app"]
    elif action == "test":
        cmd = [str(cli), "test", "app-core:hostTest", "--locked-dependencies"]

    proc = subprocess.run(cmd, cwd=PROJECT_ROOT, env=env, check=False)
    elapsed = int(time.monotonic() - started)
    print(f"Lightbuild {action} finished in {elapsed}s (exit {proc.returncode})")

    if action == "build" and proc.returncode == 0:
        apk = PROJECT_ROOT / "app/build/app-debug.apk"
        if not apk.is_file():
            return fail(f"Build succeeded but expected APK is missing: {apk}")
        print(f"APK: {apk}")

    return proc.returncode


if __name__ == "__main__":
    raise SystemExit(main())
