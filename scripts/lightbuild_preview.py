#!/usr/bin/env python3
"""Compatibility wrapper for all_scripts.lightbuild_preview."""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent / "src"))
from all_scripts.lightbuild_preview import main

if __name__ == "__main__":
    raise SystemExit(main())
