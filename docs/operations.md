# Local Android work

Prepare a JDK supported by the checked-in Gradle wrapper, Android SDK, and the existing local SDK/provider configuration. The JVM compilation target is 17; it is not a command to replace the installed Gradle runtime. Preserve local.properties, local.defaults.properties, Firebase files, and signing material without printing their values.

Inspect settings.gradle.kts and the changed modules' build.gradle.kts files to determine ownership and available task types. For changed Android modules with the permitted task, invoke from this repository root, combining targets:

```sh
./gradlew :app-arducon:testDebugUnitTest :app-cnubus:testDebugUnitTest
```

The example applies only when both modules changed. If a changed unit lacks that task, report unavailable verification. Do not substitute assemble, lint, root test, connected checks, diff-check, or native/platform builds without an explicit request.

Dependency changes belong in gradle/libs.versions.toml and the appropriate convention/module configuration. Do not run an installation or regenerate locks merely to validate prose. Build/release workflows are per app; dispatching releases or changing signing/provider credentials is separate authorized work. The Rust crate, TypeSpec package, and Xcode shell require their own prerequisites and explicit verification scope rather than an assumed Android check.

## Android CLI Tooling & Workflows

The `android` CLI (`/Users/keelim/.local/bin/android`) integrates SDK management, device/emulator lifecycle, project metadata discovery, application deployment, and journey-based UI inspection.

### 1. Environment Diagnostics & SDK Alignment
Check local SDK directory and CLI version:
```sh
android info
```
List installed SDK platforms, build tools, emulator, and system images:
```sh
android sdk list
```
Install or update SDK components (e.g., SDK 37 packages):
```sh
android sdk install build-tools/37.0.0 sources/android-37.0
android sdk update platform-tools
android sdk update cmdline-tools/latest
```

### 2. Android CLI Skills Management
List installed and available agent skills:
```sh
android skills list --agent=antigravity
```
Install required project skills:
```sh
android skills add navigation-3 navigation-event appfunctions adaptive edge-to-edge styles glance-canonical-layouts play-policy-insights r8-analyzer android-profiler android-intent-security android-permissions-security
```

### 3. Project Metadata & Build Target Indexing
Inspect monorepo application modules, build variants, and output APK artifacts across the repository:
```sh
android describe --project_dir=.
```
This indexes the 6 core application build targets:
- `:app-nanda`: `app-nanda/build/outputs/apk/debug/app-nanda-debug.apk` (`com.keelim.nandadiagnosis`)
- `:app-cnubus`: `app-cnubus/build/outputs/apk/debug/app-cnubus-debug.apk` (`com.keelim.cnubus`)
- `:app-comssa`: `app-comssa/build/outputs/apk/debug/app-comssa-debug.apk` (`com.keelim.comssa`)
- `:app-my-grade`: `app-my-grade/build/outputs/apk/debug/app-my-grade-debug.apk` (`com.keelim.mygrade`)
- `:app-mysenior`: `app-mysenior/build/outputs/apk/debug/app-mysenior-debug.apk` (`com.keelim.mysenior`)
- `:app-arducon`: `app-arducon/build/outputs/apk/debug/app-arducon-debug.apk` (`com.keelim.arducon`)

When explicit debug APK assembly is required for deployment or E2E journey testing:
```sh
./gradlew :app-my-grade:assembleDebug :app-arducon:assembleDebug
```

### 4. Emulator Lifecycle Management
Query configured Android Virtual Devices (AVDs):
```sh
android emulator list
```
Launch an emulator in headless mode (waits until boot is complete):
```sh
android emulator start --headless medium_phone
```
Verify device connection:
```sh
adb devices
```
Gracefully shut down emulator:
```sh
android emulator stop [<device>]
# e.g.: android emulator stop emulator-5554
# Or via adb:
adb -s <serial> emu kill
```

### 5. Application Deployment & Launch
Deploy and launch a built application APK:
```sh
android run --device=<serial> --apks=<path-to-apk>
```
Alternative direct adb workflow:
```sh
adb -s <serial> install -r <path-to-apk>
adb -s <serial> shell am start -n <package-id>/<main-activity-path>
```
App Entry Activities:
- `:app-nanda`: `com.keelim.nandadiagnosis/com.keelim.nandadiagnosis.ui.screen.main.MainActivity`
- `:app-cnubus`: `com.keelim.cnubus/com.keelim.cnubus.ui.screen.main.MainActivity`
- `:app-comssa`: `com.keelim.comssa/com.keelim.comssa.ui.screen.main.MainActivity`
- `:app-my-grade`: `com.keelim.mygrade/com.keelim.mygrade.ui.screen.MainActivity`
- `:app-mysenior`: `com.keelim.mysenior/com.keelim.mysenior.MainActivity`
- `:app-arducon`: `com.keelim.arducon/com.keelim.arducon.MainActivity`

### 6. Journey E2E UI Testing & Layout Inspection
Journey XML test definitions reside under `journeys/<app>-journey.xml` and test multi-step user interaction flows.
Capture formatted layout tree:
```sh
android layout --pretty
# Or flat hierarchy:
android layout --flat
```
*Note*: The `-d, --diff` flag is deprecated in `android layout` and operates as a no-op; capture snapshots directly.

Annotate screen elements and resolve interaction coordinates:
```sh
android screen capture --annotate -o /tmp/screen_annotated.png
android screen resolve --screenshot=/tmp/screen_annotated.png --string "#<index>"
```
Execute user inputs on target device:
```sh
# Tap element coordinates
adb -s <serial> shell input tap <x> <y>

# Swipe or scroll
adb -s <serial> shell input swipe <x1> <y1> <x2> <y2> <duration_ms>

# Key events (e.g., BACK = 4, HOME = 3)
adb -s <serial> shell input keyevent 4
```
Generate structured evaluation JSON reports saved in `journeys/reports/<app>-report.json` recording pass/fail assertions per interaction step.

## Lightweight Mac build (Android CLI + Gradle Wrapper)

Android Studio does not need to run for these commands. Use the existing Android
CLI for SDK discovery/diagnostics and the checked-in Gradle Wrapper for compilation.
From the repository root:

```sh
bash script/android-light.sh doctor
bash script/android-light.sh check                 # CNUBUS debug compilation, no APK packaging
bash script/android-light.sh apk                   # CNUBUS debug APK
bash script/android-light.sh test                  # CNUBUS JVM unit tests
bash script/android-light.sh apk app-my-grade      # select one of the six Android apps
```

The script reads the exact Temurin JDK pin from `mise.toml` and uses its existing
mise installation without trusting configs or auto-installing workspace tools.
For another installation of the same JDK, set `LIGHT_JAVA_HOME`. It obtains the SDK
path through `android info sdk`. Preserve the existing `local.properties` SDK path;
if that points to another SDK, align it manually before building.

Required existing tools/packages:

- Android CLI on PATH, the JDK pinned in `mise.toml`, and Gradle Wrapper 9.4.1.
- Platform `platforms/android-37.0` (API 37 minor 0 from the version catalog).
- `build-tools/36.0.0`, the default required by AGP 9.2.1, and command-line tools.
  Installed Build Tools 37 can coexist; SDK API 37 alone does not require them.
- Existing SDK license acceptance and the normal private provider configuration.

`doctor` checks installed prerequisites; it does not validate license hashes or
prove dependency resolution. Missing tools/licenses require explicit setup and
license review. The script neither installs packages nor accepts licenses, and
Gradle SDK auto-download is disabled. Existing Firebase/provider and signing
configuration is reused without printing it.

Build invocations limit Gradle to two workers, disable parallel project and
configuration-cache execution, run Kotlin compilation in-process, and use
`--no-daemon` so the single-use Gradle daemon exits afterward. Existing 2 GiB
Gradle heap settings, incremental compilation, build cache, and configuration
cache remain in effect. This limits concurrency; it is not a total RAM cap.
Set `LIGHT_WORKERS=1` for a tighter CPU limit. Cold builds may need network access
for dependencies. No emulator or system image is needed for compilation/APK tasks.

Successful APK output is printed, normally:
`app-cnubus/build/outputs/apk/debug/app-cnubus-debug.apk`.
`check` verifies compilation only; `test` runs JVM tests, and neither verifies
provider services or device behavior. No clean task is run, preserving incremental
outputs. The script reports elapsed build time and propagates Gradle failures.

References: [command-line builds](https://developer.android.com/build/building-cmdline)
and [AGP 9.2 compatibility](https://developer.android.com/build/releases/agp-9-2-0-release-notes).

### Verification baseline (2026-11-01)

The CNUBUS debug compile/APK and 12 JVM tests passed on the existing local tree
at `7caf5780c`. Script invocations took 40.3s for compile and 35.0s for APK;
a separate forced unit-test execution took 41.8s. These are cached-environment
examples, not performance guarantees. Other apps and device/provider execution
remain unverified; the existing ASM instrumentation warning remains.

On upstream `develop` at `bb9fd47de`, `doctor` passes but `check` stops before
compilation because the existing catalog selects
`androidx.appfunctions:appfunctions-service:1.0.0-beta01`, which Google Maven
returns as HTTP 404. Its published metadata lists alpha01 through alpha09.
APK/test verification on that baseline remains blocked by dependency resolution.
This CLI setup does not change the catalog or Gradle Wrapper to work around it.
