import tempfile
import unittest
from pathlib import Path
from all_scripts.check_baseline_profiles import check


class BaselineProfileTest(unittest.TestCase):
    def test_copied_baseline_and_startup_profiles_are_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            folder = root / "app-test/src/main"
            folder.mkdir(parents=True)
            bad = folder / "baseline-prof.txt"
            bad.write_text("HLcom/google/samples/apps/nowinandroid/MainActivity;\n")
            startup = folder / "startup-prof.txt"
            startup.write_text(bad.read_text())
            self.assertEqual(check(root), [bad, startup])

    def test_app_and_dependency_profiles_are_retained(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            folder = root / "app-test/src/main"
            folder.mkdir(parents=True)
            (folder / "baseline-prof.txt").write_text("HLcom/keelim/test/MainActivity;\nHLandroidx/compose/Test;\n")
            self.assertEqual(check(root), [])

    def test_repository_has_no_copied_sample_profile(self):
        self.assertEqual(check(Path(__file__).resolve().parents[2]), [])
