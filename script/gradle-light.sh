#!/usr/bin/env bash
# Scoped Gradle CLI workflow; never installs tools or accepts SDK licenses.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"
action="${1:-doctor}"
module="${2:-app-cnubus}"
case "$action" in
  doctor|check|apk|test) ;;
  *) echo "Usage: bash script/gradle-light.sh {doctor|check|apk|test} [app-module]" >&2; exit 2 ;;
esac
case "$module" in
  app-cnubus|app-arducon|app-comssa|app-my-grade|app-mysenior|app-nanda) ;;
  *) echo "Choose an existing Android app module (for example app-cnubus)." >&2; exit 2 ;;
esac
if [ "$#" -gt 2 ]; then echo "Too many arguments." >&2; exit 2; fi

java_tool="$(sed -n 's/^java = "\([^"]*\)"/\1/p' mise.toml)"
export JAVA_HOME="${LIGHT_JAVA_HOME:-$HOME/.local/share/mise/installs/java/$java_tool}"
if [ ! -x "$JAVA_HOME/bin/java" ]; then
  echo "Pinned JDK missing: $java_tool. Install it explicitly with mise, or set LIGHT_JAVA_HOME." >&2
  exit 1
fi
java_settings="$("$JAVA_HOME/bin/java" -XshowSettings:properties -version 2>&1)"
runtime="$(printf '%s\n' "$java_settings" | sed -n 's/^[[:space:]]*java.runtime.version = //p')"
vendor="$(printf '%s\n' "$java_settings" | sed -n 's/^[[:space:]]*java.vendor = //p')"
if [ "$runtime" != "${java_tool#temurin-}" ] || [ "$vendor" != 'Eclipse Adoptium' ]; then
  echo "Expected $java_tool (Eclipse Adoptium); found $runtime ($vendor)." >&2
  exit 1
fi
export PATH="$JAVA_HOME/bin:$PATH"
command -v android >/dev/null || { echo "Android CLI is required on PATH." >&2; exit 1; }
sdk="$(android info sdk)"
compile_sdk="$(sed -n 's/^compileSdk = "\([0-9]*\)"/\1/p' gradle/libs.versions.toml)"
if [ -z "$compile_sdk" ] || [ ! -d "$sdk/licenses" ]; then
  echo "SDK configuration/licenses missing. Review SDK setup manually; no licenses are accepted here." >&2
  exit 1
fi
if [ ! -f "$sdk/platforms/android-$compile_sdk.0/android.jar" ] &&
   [ ! -f "$sdk/platforms/android-$compile_sdk/android.jar" ]; then
  echo "Missing SDK platform $compile_sdk. Install the matching official package after license review." >&2
  exit 1
fi
# AGP 9.2 uses Build Tools 36.0.0 by default. SDK 37 does not imply Build Tools 37.
if [ ! -x "$sdk/build-tools/36.0.0/aapt2" ]; then
  echo "Missing build-tools/36.0.0 (AGP 9.2 default). Review installation explicitly." >&2
  exit 1
fi
export ANDROID_HOME="$sdk"
export ANDROID_SDK_ROOT="$sdk"
workers="${LIGHT_WORKERS:-2}"
case "$workers" in ''|*[!0-9]*|0) echo "LIGHT_WORKERS must be a positive integer." >&2; exit 2;; esac
printf 'JDK: %s (%s)\nSDK: %s\nModule: :%s; workers: %s\n' "$runtime" "$vendor" "$sdk" "$module" "$workers"
if [ "$action" = doctor ]; then
  android --version
  ./gradlew --version
  exit
fi
case "$action" in
  check) task=compileDebugSources ;;
  apk) task=assembleDebug ;;
  test) task=testDebugUnitTest ;;
esac
started=$SECONDS
result=0
./gradlew --console=plain --no-daemon --no-parallel --max-workers="$workers" \
  -Dorg.gradle.configuration-cache.parallel=false \
  -Pandroid.builder.sdkDownload=false \
  -Pkotlin.compiler.execution.strategy=in-process \
  ":$module:$task" || result=$?
printf 'Finished %s in %ss (exit %s)\n' ":$module:$task" "$((SECONDS - started))" "$result"
if [ "$result" -ne 0 ]; then exit "$result"; fi
if [ "$action" = apk ]; then
  find "$ROOT/$module/build/outputs/apk/debug" -name '*.apk' -type f -print
fi
