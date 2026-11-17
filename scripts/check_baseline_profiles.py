#!/usr/bin/env python3
"""Compatibility entrypoint for the profile provenance guard."""
import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).resolve().parent / "src"))
from all_scripts.check_baseline_profiles import main

if __name__ == "__main__":
    raise SystemExit(main())
