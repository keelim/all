"""Headless Compose review through the official Android CLI, using scoped debug targets."""
from __future__ import annotations

import argparse
from pathlib import Path
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[3]
TARGETS = {
    "a2ui": ":feature:ui-a2ui",
    "comssa": ":app-comssa",
    "cnubus": ":app-cnubus",
}


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=("list", "render"))
    parser.add_argument("--target", choices=TARGETS, default="a2ui")
    parser.add_argument("--cli", help="Existing android or android-cli executable; installs nothing")
    parser.add_argument("--fqn", action="append", default=[], help="Preview name returned by list")
    parser.add_argument("--output", type=Path, help="Render directory; defaults to a temporary directory")
    parser.add_argument("--daemon", action="store_true", help="Reuse renderer for subsequent renders")
    parser.add_argument("--no-image", action="store_true", help="Export component tree without PNG")
    args = parser.parse_args(argv)
    if args.action == "render" and not args.fqn:
        parser.error("render requires --fqn from the list output")
    if args.action == "list" and (args.fqn or args.output or args.daemon or args.no_image):
        parser.error("render options require the render action")
    executable = shutil.which(args.cli) if args.cli else (shutil.which("android") or shutil.which("android-cli"))
    if not executable:
        parser.error("Android CLI is missing; provide an existing executable with --cli")

    command = [executable, "preview", args.action, TARGETS[args.target], "--format=json"]
    if args.action == "render":
        destination = args.output.resolve() if args.output else Path(tempfile.mkdtemp(prefix="all-compose-preview-"))
        command.extend(["--component-tree", f"--output={destination}"])
        command.extend(f"--fqn={name}" for name in args.fqn)
        if args.daemon:
            command.append("--daemon")
        if args.no_image:
            command.append("--no-image")
    command.extend(["--", "-Pandroid.builder.sdkDownload=false"])
    return subprocess.run(command, cwd=ROOT, check=False).returncode


if __name__ == "__main__":
    raise SystemExit(main())
