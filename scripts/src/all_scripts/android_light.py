#!/usr/bin/env python3
"""Scoped Android CLI + Gradle Wrapper workflow; never installs tools or accepts SDK licenses."""

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
VALID_ACTIONS = {"doctor", "check", "apk", "test"}
VALID_MODULES = {
    "app-cnubus",
    "app-arducon",
    "app-comssa",
    "app-my-grade",
    "app-mysenior",
    "app-nanda",
}
TASK_MAP = {
    "check": "compileDebugSources",
    "apk": "assembleDebug",
    "test": "testDebugUnitTest",
}


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
    if len(args) > 2:
        return fail("Too many arguments.", 2)

    action = args[0] if len(args) >= 1 else "doctor"
    module = args[1] if len(args) >= 2 else "app-cnubus"

    if action not in VALID_ACTIONS:
        return fail(
            "Usage: uv run --project scripts android-light {doctor|check|apk|test} [app-module]",
            2,
        )
    if module not in VALID_MODULES:
        return fail("Choose an existing Android app module (for example app-cnubus).", 2)

    java_tool = read_regex(ROOT / "mise.toml", r'^java = "([^"]*)"')
    if not java_tool:
        return fail("Missing java tool pin in mise.toml.")

    default_java_home = Path.home() / ".local/share/mise/installs/java" / java_tool
    java_home = Path(os.environ.get("LIGHT_JAVA_HOME") or default_java_home)
    java_bin = java_home / "bin/java"
    if not (java_bin.is_file() and os.access(java_bin, os.X_OK)):
        return fail(
            f"Pinned JDK missing: {java_tool}. Install it explicitly with mise, or set LIGHT_JAVA_HOME."
        )

    java_proc = subprocess.run(
        [str(java_bin), "-XshowSettings:properties", "-version"],
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
        check=False,
    )
    runtime_match = re.search(
        r"^\s*java\.runtime\.version = (.+)$", java_proc.stdout, re.MULTILINE
    )
    vendor_match = re.search(
        r"^\s*java\.vendor = (.+)$", java_proc.stdout, re.MULTILINE
    )
    runtime = runtime_match.group(1).strip() if runtime_match else ""
    vendor = vendor_match.group(1).strip() if vendor_match else ""
    expected_runtime = java_tool.removeprefix("temurin-")
    if runtime != expected_runtime or vendor != "Eclipse Adoptium":
        return fail(
            f"Expected {java_tool} (Eclipse Adoptium); found {runtime} ({vendor})."
        )

    env = os.environ.copy()
    env["JAVA_HOME"] = str(java_home)
    env["PATH"] = f"{java_home / 'bin'}:{env.get('PATH', '')}"

    android_bin = shutil.which("android", path=env["PATH"])
    if not android_bin:
        return fail("Android CLI is required on PATH.")

    sdk_proc = subprocess.run(
        [android_bin, "info", "sdk"],
        env=env,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True,
        check=False,
    )
    sdk_str = sdk_proc.stdout.strip()
    sdk = Path(sdk_str) if sdk_str else Path()
    compile_sdk = read_regex(
        ROOT / "gradle/libs.versions.toml", r'^compileSdk = "([0-9]+)"'
    )
    if not compile_sdk or not (sdk / "licenses").is_dir():
        return fail(
            "SDK configuration/licenses missing. Review SDK setup manually; no licenses are accepted here."
        )

    platform_jar_dot = sdk / f"platforms/android-{compile_sdk}.0/android.jar"
    platform_jar_plain = sdk / f"platforms/android-{compile_sdk}/android.jar"
    if not platform_jar_dot.is_file() and not platform_jar_plain.is_file():
        return fail(
            f"Missing SDK platform {compile_sdk}. Install the matching official package after license review."
        )

    aapt2 = sdk / "build-tools/36.0.0/aapt2"
    if not (aapt2.is_file() and os.access(aapt2, os.X_OK)):
        return fail(
            "Missing build-tools/36.0.0 (AGP 9.2 default). Review installation explicitly."
        )

    env["ANDROID_HOME"] = str(sdk)
    env["ANDROID_SDK_ROOT"] = str(sdk)

    workers_raw = env.get("LIGHT_WORKERS", "2")
    if not workers_raw.isdigit() or int(workers_raw) <= 0:
        return fail("LIGHT_WORKERS must be a positive integer.", 2)

    print(
        f"JDK: {runtime} ({vendor})\n"
        f"SDK: {sdk}\n"
        f"Module: :{module}; workers: {workers_raw}"
    )

    gradlew = ROOT / "gradlew"
    if action == "doctor":
        res1 = subprocess.run([android_bin, "--version"], cwd=ROOT, env=env, check=False)
        if res1.returncode != 0:
            return res1.returncode
        res2 = subprocess.run([str(gradlew), "--version"], cwd=ROOT, env=env, check=False)
        return res2.returncode

    task = TASK_MAP[action]
    started = time.monotonic()
    cmd = [
        str(gradlew),
        "--console=plain",
        "--no-daemon",
        "--no-parallel",
        f"--max-workers={workers_raw}",
        "-Dorg.gradle.configuration-cache.parallel=false",
        "-Pandroid.builder.sdkDownload=false",
        "-Pkotlin.compiler.execution.strategy=in-process",
        f":{module}:{task}",
    ]
    proc = subprocess.run(cmd, cwd=ROOT, env=env, check=False)
    elapsed = int(time.monotonic() - started)
    print(f"Finished :{module}:{task} in {elapsed}s (exit {proc.returncode})")
    if proc.returncode != 0:
        return proc.returncode

    if action == "apk":
        apk_dir = ROOT / module / "build/outputs/apk/debug"
        if apk_dir.is_dir():
            for apk in sorted(apk_dir.rglob("*.apk")):
                if apk.is_file():
                    print(apk)

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
