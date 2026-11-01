#!/usr/bin/env bash
# Native Google Lightbuild workflow for the independent preview project.
set -euo pipefail
repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
project_root="$repo_root/previews/lightbuild"
action="${1:-doctor}"
if [ "$#" -gt 1 ]; then echo "Usage: bash script/lightbuild-preview.sh {doctor|query|resolve|build|test}" >&2; exit 2; fi
case "$action" in doctor|query|resolve|build|test) ;; *) echo "Unknown action: $action" >&2; exit 2;; esac
command -v android >/dev/null || { echo "Android CLI is required on PATH." >&2; exit 1; }
sdk="$(android info sdk)"
version="$(sed -n 's/^  version: \([^ ]*\)/\1/p' "$project_root/project.lightbuild.yaml")"
if [ -z "$version" ]; then echo "Missing pinned Lightbuild version." >&2; exit 1; fi
distribution="$sdk/build/lightbuild/$version"
if [ ! -f "$distribution/source.properties" ] || [ ! -x "$distribution/lightbuild-daemon" ]; then
  echo "Install the official SDK package explicitly: android sdk install build/lightbuild/$version" >&2
  echo "Review any new license prompt before accepting. This script installs nothing." >&2
  exit 1
fi
if ! grep -Fqx "Pkg.Revision=$version" "$distribution/source.properties"; then
  echo "The installed package does not match the pinned Lightbuild version." >&2; exit 1
fi
# Use the matching package's CLI, avoiding another global CLI/runtime version.
cli="$distribution/cli/lightbuild"
if [ ! -x "$cli" ]; then echo "Missing Lightbuild CLI in the pinned SDK package." >&2; exit 1; fi
java_tool="$(sed -n 's/^java = "\([^"]*\)"/\1/p' "$repo_root/mise.toml")"
export JAVA_HOME="${LIGHT_JAVA_HOME:-$HOME/.local/share/mise/installs/java/$java_tool}"
if [ ! -x "$JAVA_HOME/bin/java" ]; then echo "Missing existing JDK: $java_tool; set LIGHT_JAVA_HOME." >&2; exit 1; fi
java_settings="$("$JAVA_HOME/bin/java" -XshowSettings:properties -version 2>&1)"
java_major="$(printf '%s\n' "$java_settings" | sed -n 's/^[[:space:]]*java.specification.version = //p')"
if [ "$java_major" != 17 ]; then echo "The preview requires an installed JDK 17." >&2; exit 1; fi
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_HOME="$sdk"
export ANDROID_SDK_ROOT="$sdk"
cd "$project_root"
printf 'Independent preview: %s\nLightbuild SDK package: %s\n' "$project_root" "$version"
if [ "$action" = doctor ]; then
  "$cli" --version
  "$JAVA_HOME/bin/java" -version
  exit
fi
started=$SECONDS
result=0
case "$action" in
  query) "$cli" query ... || result=$? ;;
  resolve) "$cli" resolve || result=$? ;;
  build) "$cli" build app || result=$? ;;
  test) "$cli" test app-core:hostTest --locked-dependencies || result=$? ;;
esac
printf 'Lightbuild %s finished in %ss (exit %s)\n' "$action" "$((SECONDS - started))" "$result"
if [ "$action" = build ] && [ "$result" -eq 0 ]; then
  apk="$project_root/app/build/app-debug.apk"
  if [ ! -f "$apk" ]; then echo "Build succeeded but expected APK is missing: $apk" >&2; exit 1; fi
  printf 'APK: %s\n' "$apk"
fi
exit "$result"
