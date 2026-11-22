import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch
from all_scripts import compose_preview


class ComposePreviewTest(unittest.TestCase):
    @patch("all_scripts.compose_preview.subprocess.run")
    @patch("all_scripts.compose_preview.shutil.which", return_value="/tools/android-cli")
    def test_list_uses_scoped_target_and_debug_build_arguments(self, which, run):
        run.return_value.returncode = 0
        self.assertEqual(compose_preview.main(["list"]), 0)
        command = run.call_args.args[0]
        self.assertEqual(command, ["/tools/android-cli", "preview", "list", ":feature:ui-a2ui:impl", "--format=json", "--", "-Pandroid.builder.sdkDownload=false"])
        self.assertFalse(run.call_args.kwargs.get("shell", False))

    @patch("all_scripts.compose_preview.subprocess.run")
    @patch("all_scripts.compose_preview.shutil.which", return_value="/tools/android")
    def test_render_preserves_fqn_as_one_argument_and_propagates_failure(self, which, run):
        run.return_value.returncode = 7
        with tempfile.TemporaryDirectory() as folder:
            result = compose_preview.main(["render", "--target", "comssa", "--fqn", "pkg.ScreenKt.Preview", "--fqn", "literal;echo unsafe", "--output", folder, "--daemon", "--no-image"])
            command = run.call_args.args[0]
            self.assertEqual(result, 7)
            self.assertIn(":app-comssa", command)
            self.assertIn("--fqn=literal;echo unsafe", command)
            self.assertIn("--component-tree", command)
            self.assertIn("--daemon", command)
            self.assertIn(f"--output={Path(folder).resolve()}", command)

    def test_reject_release_targets_and_unsupported_build_arguments(self):
        for argv in (["list", "--target", ":app-comssa:assembleRelease"], ["list", "--", "publish"], ["render"], ["list", "--daemon"]):
            with self.assertRaises(SystemExit) as error:
                compose_preview.main(argv)
            self.assertEqual(error.exception.code, 2)

    @patch("all_scripts.compose_preview.shutil.which", return_value=None)
    def test_missing_cli_does_not_install_anything(self, which):
        with self.assertRaises(SystemExit) as error:
            compose_preview.main(["list"])
        self.assertEqual(error.exception.code, 2)

    @patch("all_scripts.compose_preview.subprocess.run")
    @patch("all_scripts.compose_preview.shutil.which", side_effect=[None, "/tools/android-cli"])
    def test_android_cli_binary_name_is_supported(self, which, run):
        run.return_value.returncode = 0
        self.assertEqual(compose_preview.main(["list", "--target", "cnubus"]), 0)
        self.assertEqual(run.call_args.args[0][0], "/tools/android-cli")
