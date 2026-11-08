#!/usr/bin/env python3
"""Compatibility wrapper for tests.test_module_dependency_graph."""

import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent / "src"))
sys.path.insert(0, str(Path(__file__).resolve().parent))
from tests.test_module_dependency_graph import ModuleGraphTest

if __name__ == "__main__":
    unittest.main()
