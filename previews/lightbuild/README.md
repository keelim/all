# Google Lightbuild preview alongside Gradle

This is an independent **sample** project, not a Lightbuild port of CNUBUS or
another production app. The same Git repository manages two separate build roots:

| Root | Build entry point | Ownership |
| --- | --- | --- |
| Repository root | `bash script/gradle-light.sh ...` or `./gradlew` | Existing Android/KMP products |
| `previews/lightbuild` | `bash script/lightbuild-preview.sh ...` | New Lightbuild sample only |

The root `settings.gradle.kts`, version catalog, convention plugins, and Gradle
Wrappers remain unchanged. The preview has its own YAML module graph, Kotlin pin,
dependency lockfiles, and outputs. It never includes the root Gradle modules.
Lightbuild may generate internal build machinery; this does not join the two graphs.

Lightbuild 0.0.30 is an experimental preview. It does not support the existing
project's version catalogs, convention/custom plugins, KMP modules, or mixing
Kotlin DSL and Lightbuild modules in one project. Co-management here means separate
projects in one Git repository, not partial migration of the existing module graph.

## One-time environment setup

Use the installed Google Android CLI and an explicitly installed JDK matching
the root `mise.toml`. The wrapper uses that existing mise JDK by default;
`LIGHT_JAVA_HOME` selects an alternate installed JDK 17. The sample pins Java 17,
Kotlin 2.3.20, SDK 37.0, and Lightbuild 0.0.30 independently of the product catalog.

After reviewing any new license prompt, install the official SDK package:

```sh
android sdk install build/lightbuild/0.0.30
```

The project wrapper invokes the matching package's `cli/lightbuild` directly,
so no shell profile change or global CLI copy is required. To use the vendor's
standalone `lightbuild` command too, follow the official manual installation:

```sh
cp "$(android info sdk)/build/lightbuild/0.0.30/cli/lightbuild" "$HOME/.local/bin/lightbuild"
chmod u+x "$HOME/.local/bin/lightbuild"
```

Check for an existing destination before copying. `~/.local/bin` must already be
on PATH, or use the package CLI through this repository's wrapper instead.
The vendor CLI launcher version (currently 0.0.1) is distinct from the pinned
build distribution version (0.0.30). Neither helper installs SDK packages or
accepts licenses. First use unpacks vendor runtimes into `~/.android/lightbuild`;
first builds also download compiler/build dependencies. The native runtime's
resource and daemon behavior is vendor-managed, not the Gradle helper's worker cap.

## Commands from the repository root

```sh
bash script/lightbuild-preview.sh doctor
bash script/lightbuild-preview.sh query
bash script/lightbuild-preview.sh resolve
bash script/lightbuild-preview.sh build
bash script/lightbuild-preview.sh test
```

- `query` discovers the actual targets with `lightbuild query ...`.
- `resolve` regenerates the two `resolved.deps` files; commit them without editing.
- `build` assembles only the sample's `app` debug APK.
- `test` runs only `app-core:hostTest`, with locked dependencies. It never selects
  all tests implicitly, because that could include device tests.
- Missing packages/JDKs fail with guidance. The commands do not install an APK,
  launch an emulator, sign a release, or publish anything.

The `app` module is an assembly shell. Kotlin sources and the launcher activity
are in the Android library `app-core`, following the documented Lightbuild layout.
Five host tests check a sample verification-summary rule: empty, failing, skipped,
or invalid runs must not be reported as fully verified. This is sample domain
logic, not live telemetry from the parent repository's tests.

This example uses platform Android widgets to keep its dependency footprint small.
It does not establish Compose, KSP, Hilt, Firebase, or KMP compatibility for `all`.
The installed Android CLI 1.0.16625370 does not expose the Lightbuild create template;
the checked-in configuration was authored from the official schema instead of
enabling hidden flags or upgrading the CLI.

## Verification (2026-11-01, Mac ARM64)

Using Android CLI 1.0.16625370, Lightbuild SDK distribution 0.0.30 / CLI launcher
0.0.1, Temurin 17.0.20+8, Kotlin 2.3.20, and the existing SDK 37.0:

| Operation | Observed result |
| --- | --- |
| `doctor` | Both Lightbuild versions and JDK reported successfully |
| `query ...` | App APK/AAB, library AAR, and host/device test targets discovered; wrapper reported 4s |
| `resolve` | Both generated lockfiles created; 3.5s wall time |
| `build app` | Debug APK built; 66.9s wall time (vendor-reported total 66.0s) |
| Repeated `build app` | 2.7s wall time with existing outputs; wrapper prints APK path |
| `test app-core:hostTest --locked-dependencies` | 5 tests passed, 0 failures/errors/skips; 10.0s wall time (vendor-reported total 7.4s) |

Times include the local environment and cache state; they are not guarantees.
The package referenced the already accepted `android-sdk-license`; installation
presented no new license prompt and existing license files were unchanged.
Android CLI also fetched its official `build/templates` package during template
discovery. No Android CLI upgrade, hidden flags, emulator, or system image was used.

Outputs, relative to this project root:

- APK: `app/build/app-debug.apk` (906,215 bytes at verification).
- SHA-256: `74051c1ad632f3cdb2802acef97b760adb59cd25c4f0e3a1a22e724289715818`.
- XML test report:
  `app-core/build/intermediates/unit_test_results/debug/testDebugUnitTest/TEST-com.keelim.lightbuildpreview.VerificationSummaryTest.xml`.
- The packaged manifest contains the declared MAIN/LAUNCHER activity. No device
  install, launch, visual verification, or production-app compatibility is claimed.

The parent Gradle build remains a separate validation boundary. Its existing
`appfunctions-service:1.0.0-beta01` dependency resolution failure on develop
`bb9fd47de` was not repaired or used by this sample.

## Official references

- [Setup](https://developer.android.com/tools/agents/lightbuild/setup)
- [CLI installation and commands](https://developer.android.com/tools/agents/lightbuild/commands)
- [Project layout](https://developer.android.com/tools/agents/lightbuild/structure)
- [YAML schema](https://developer.android.com/tools/agents/lightbuild/schema)
- [Preview limitations](https://developer.android.com/tools/agents/lightbuild/release-notes)
