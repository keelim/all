# Unresolved findings

## app-nanda debug build verification (2026-11-10 KST)

The explicitly requested `:app-nanda:assembleDebug` failed during dependency resolution with JDK 17: `:app-nanda:debugCompileClasspath` could not resolve `:core:component` because `Cannot determine the version of Skiko for Compose '1.10.1'`. No app compilation or new APK was verified. Log: `/tmp/app-nanda-build-java17.log` (session-local). The default Java 21 installation lacks `javac`; this prerequisite was bypassed using the existing `/workspace/.tools/java17` JDK. Gradle downloads required JVM proxy properties derived from the supplied proxy environment.

Root cause confirmed by resolving the same detached `org.jetbrains.compose.ui:ui-graphics:1.10.1` dependency from `:core:component`: the JDK 17 trust store rejects the proxied Maven Central TLS certificate (`certificate_unknown`, `PKIX path building failed`). Compose's web configuration uses lenient dependency resolution to discover Skiko, masking the underlying TLS failure with the generic version error. The shared multiplatform convention enables executable Wasm targets, so this web setup is evaluated even during Android dependency resolution. With the same JDK and proxy settings plus `-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts`, the diagnostic resolution succeeded and found `org.jetbrains.skiko:skiko:0.9.37.4`. This confirms the immediate failure is environmental rather than a missing Skiko version; a subsequent APK build with corrected trust settings passed this dependency resolution stage. Evidence: `/tmp/skiko-resolution.log` and `/tmp/skiko-resolution-trusted.log` (session-local). No runtime source or dependency versions were changed.

With the pinned JDK, system trust store, proxy settings, and writable `ANDROID_USER_HOME`, the subsequent `:app-nanda:assembleDebug` failed at `:core:network:compileDebugJavaWithJavac`. Generated `BuildConfig.java` contains empty expressions for `AD_NANDA_APPLICATION_ID`, `AD_NANDA_BANNER_ID`, and `AD_NANDA_TEST_BANNER_ID` (`public static final String ... = ;`). Required local advertising configuration must be supplied before a complete APK can be verified. No provider values were created or changed. Log: `/tmp/app-nanda-aligned-build.log` (session-local).

Native Rust loading, Xcode linkage, and Compose platform execution were not established by the inspected manifests. A source host or declared target is not proof of a working end-to-end build. These require explicit platform verification scope and prerequisites before runtime claims.

Some modules do not expose the repository's permitted testDebugUnitTest task. That is a verification limitation, not permission to run an alternate build. Existing local product changes remain outside documentation edits. Specifically:
- `:core:navigation`: pure JVM library (`keelim.jvm.library`) configured with standard Kotlin/JVM plugins; does not register the Android `testDebugUnitTest` variant task.
- `:core:component`: Kotlin Multiplatform library (`keelim.multiplatform`); does not expose `testDebugUnitTest`.
- `:build-logic`: composite build (`includeBuild("build-logic")`) convention plugins; not a root subproject with `testDebugUnitTest`.
Per repository hard gates (`AGENTS.md`, `docs/standards.md`, `docs/operations.md`), running substitute tasks (such as `test`, `desktopTest`, `assembleDebug`, `lint`, or root `test`) for these modules is strictly prohibited.

## Android CLI & Environment Findings

### 1. macOS Sandboxing and File System Permissions
Under macOS agent execution environments, executing Android CLI operations or emulator utilities within sandboxed subshells without filesystem permissions can result in `Operation not permitted` or missing path errors when reading the user-level SDK (`~/Library/Android/sdk`) and AVD definitions (`~/.android/avd/`). All `android` CLI commands must be executed with sandbox bypass (`BypassSandbox: true`) using the canonical CLI wrapper path `~/.local/bin/android`.

### 2. Multi-App Debug APK Artifact Status
Inspection via `android describe --project_dir=.` established the current debug APK artifact presence across the 6 applications:
- **Pre-existing debug APKs on disk**:
  - `:app-nanda`: `app-nanda/build/outputs/apk/debug/app-nanda-debug.apk` (Present)
  - `:app-cnubus`: `app-cnubus/build/outputs/apk/debug/app-cnubus-debug.apk` (Present)
  - `:app-comssa`: `app-comssa/build/outputs/apk/debug/app-comssa-debug.apk` (Present)
  - `:app-mysenior`: `app-mysenior/build/outputs/apk/debug/app-mysenior-debug.apk` (Present)
  - `:catalog`: `catalog/build/outputs/apk/debug/catalog-debug.apk` (Present)
- **Missing debug APKs on disk**:
  - `:app-my-grade`: `app-my-grade/build/outputs/apk/debug/app-my-grade-debug.apk` (Does NOT exist on disk)
  - `:app-arducon`: `app-arducon/build/outputs/apk/debug/app-arducon-debug.apk` (Does NOT exist on disk)
- **Operational consequence**:
  While routine module verification strictly forbids running `assembleDebug` in place of `testDebugUnitTest`, running E2E Journey test execution against the live emulator requires building missing APKs. Specifically, `:app-my-grade:assembleDebug` and `:app-arducon:assembleDebug` must be built explicitly prior to live deployment on the emulator.

### 3. Android Layout CLI Diff Deprecation
In `android` CLI version 1.0.16625370, the `-d, --diff` option in `android layout` has been deprecated and acts as a no-op. Layout verification workflows must not rely on `--diff`; instead, layout verification must evaluate full formatted dumps (`android layout --pretty` or `--flat`) or compute structured comparisons programmatically.

### 4. Location permission cleanup verification (2026-11-16)

On base `8d500a214`, the changed-app verification command
`./gradlew :app-cnubus:testDebugUnitTest :app-arducon:testDebugUnitTest`
cannot complete because `:feature:app-function` requires
`androidx.appfunctions:appfunctions-service:1.0.0-rc01`, which Gradle cannot
find in Google Maven, Maven Central, or JitPack. The requested debug main-manifest
generation is blocked by the same runtime dependency. Source manifests were
checked, but final merged permissions and device behavior remain unverified;
existing APKs and older merged manifests are not evidence for this change.
Resolve the dependency in its owning module before repeating these checks.

## Baseline Profile cleanup verification (2026-11-17)

The combined `:app-cnubus:testDebugUnitTest :app-comssa:testDebugUnitTest :app-mysenior:testDebugUnitTest` invocation is blocked while resolving the existing `androidx.appfunctions:appfunctions-service:1.0.0-rc01` dependency from `:feature:app-function`. The fallback JitPack request returns HTTP 403; the failure occurs before app tests execute. Profile provenance host tests pass (3 tests). No replacement Baseline Profiles or startup improvements can be verified without a producer and device. See session-local `/tmp/baseline-checks.log`. `scripts` has no Android `testDebugUnitTest` task; its focused host tests are run through Python unittest.

A separate MySenior-only run with `--no-configuration-cache` passed AppFunctions
dependency selection but stopped at `:core:network:compileDebugJavaWithJavac`.
The existing local advertising values generate empty Java expressions for
`AD_NANDA_APPLICATION_ID`, `AD_NANDA_BANNER_ID` and `AD_NANDA_TEST_BANNER_ID`.
No new provider values were fabricated or configured. The app tests and R8
minification remain unverified. Session-local logs:
`/tmp/baseline-mysenior-checks.log` and `/tmp/r8-mysenior-checks.log`.

## R8 serialization keep-rule verification (2026-11-17)

The combined six-app debug unit test and MySenior minification invocation is blocked during existing AppFunctions service dependency resolution (`androidx.appfunctions:appfunctions-service:1.0.0-rc01`, JitPack HTTP 403). This is not evidence of a keep-rule regression; debug tests also cannot establish minified runtime serialization correctness. Session-local log: `/tmp/r8-checks.log`. Consumer rules were inspected in the existing `kotlinx-serialization-core-jvm:1.11.0` artifact. No size reduction or on-device round-trip success is claimed.

A separate MySenior-only run with `--no-configuration-cache` passed AppFunctions
dependency selection but stopped at `:core:network:compileDebugJavaWithJavac`.
The existing local advertising values generate empty Java expressions for
`AD_NANDA_APPLICATION_ID`, `AD_NANDA_BANNER_ID` and `AD_NANDA_TEST_BANNER_ID`.
No new provider values were fabricated or configured. The app tests and R8
minification remain unverified. Session-local logs:
`/tmp/baseline-mysenior-checks.log` and `/tmp/r8-mysenior-checks.log`.

## Compose list identity verification (2026-11-17)

`:app-cnubus:testDebugUnitTest :app-comssa:testDebugUnitTest :app-cnubus:lintDebug :app-comssa:lintDebug` is blocked before app compilation/tests by existing `androidx.appfunctions:appfunctions-service:1.0.0-rc01` resolution (JitPack HTTP 403) during configuration-cache task graph serialization. The new key regression tests have not executed, and no device frame-time measurement is available. Session-local log: `/tmp/lists-checks.log`.

## Device KMP verification (2026-11-22)

The Android-KMP library `:core:device` has no `testDebugUnitTest` task. Its
Android sources and Hilt bindings are exercised through the consumer app builds.
The module layout is covered by `:feature:ui-setting:testDebugUnitTest`.

## Kui market-up chip contrast (2026-11-22)

`@google/design.md@0.4.0 lint DESIGN.md` reports a `contrast-ratio` warning:
Finance `KuiColors.up` (`#DC2626`) on `upSoft` (`Color(0x1ADC2626)`, about
`#FBE9E9` over white) measures 4.12:1, below WCAG AA 4.5:1 for body text.
Reproduce with `mise run design-lint`. Impact: red market-up chips and small
up-delta text may be hard to read. Unresolved because retuning
`core:designsystem` market tokens changes every consuming app's screens and
needs a separate, owner-approved token change; `DESIGN.md` documents the gap
and requires a sign or arrow alongside color.

## Common Android build repair validation (2026-11-22)

The repair uses the stable `platforms;android-37.2` SDK in both Android convention paths,
and `androidx.appfunctions:appfunctions:1.0.0` with generated service entry points.
The former standalone `appfunctions-service:1.0.0` coordinate does not exist.
The original abstract class names are retained to preserve external function IDs;
JSON validation, output formatting, and the neutral health status model remain unchanged.
Public/synthetic offline fixtures are generated only in the isolated CI checkout.

The nested build-logic wrapper originally pointed to Gradle 9.9.2 with the 9.8.2
checksum. Running the repository wrapper launcher against those nested properties
reproduced checksum verification failure. The official 9.9.2 checksum
`971fcd7150ca4ba04852c0b2ca0d2d0ab9468e14b03473eab19528809914c76b`
then passed an actual distribution download and `--version` on the pinned JDK 17.

`Common Android build validation` performs clean shared-module and six Android app
compilation/tests (including the four AppFunctions consumers), checks generated function IDs/models and APK metadata, and requires
actual discovery of the API 36 Robolectric service tests. Robolectric 4.16.1 supports
API 36 with a Java 21 test runner; compilation still uses the repository Java 17 target.
API 37 device discovery and real agent invocation are not established by these tests.
No production secrets, deployment, release signing or targetSdk changes are involved.

The separate Nanda `:app-nanda:dependencyGuard` release-runtime baseline drift in
[diagnostic CI 44988946234](https://github.com/keelim/all/actions/runs/44988946234)
is outside this common-build repair. Its check and baseline are unchanged; review
its exact dependency diff separately before any dependency PR merge. No broad
baseline regeneration or check suppression is part of this repair.

The first repair CI exposed empty `GENERATIVE` and `SHEET_KEY` values becoming invalid
Java expressions in generated BuildConfig. CI uses explicit non-empty synthetic
`offline-placeholder` values for these fields; no working provider key is used.

Consumer validation reached an independent existing Arducon compiler failure in
`CreateDeepLinkScreen.kt:218,222`: `menuAnchor()` requires a `type` and
`ExposedDropdownMenu` is unresolved in the current wrapper scope. The same obsolete calls also exist in the CNUBUS dropdown preview. The pinned
Material3 1.6.0-alpha01 sources confirm that the former member menu is hidden in
favor of an imported scope extension and `menuAnchor` now requires a type. Both
read-only fields use `PrimaryNotEditable`; only the import and anchor API binding
are changed, preserving selection callbacks and UI behavior. CI uses Gradle `--continue` to exercise the
other consumers while preserving the failing exit status; it never treats this
failure as passing. Direct develop push remains blocked until consumer validation passes.
Evidence: https://github.com/keelim/all/actions/runs/44996494354/job/137128529092 .

The combined six-app CI build exposed D8 `Java heap space` during MyGrade dex
merging with the repository's 2 GiB Gradle heap. Comssa, Nanda and MySenior debug
APKs built, and Comssa's AppFunctions APK metadata passed. Consumer CI alone uses
a 4 GiB heap, one worker and in-process Kotlin compilation to control peak memory;
the repository's local Gradle defaults remain unchanged. This is a resource
configuration adjustment, not a test exclusion or dependency-version change.
Evidence: https://github.com/keelim/all/actions/runs/44997329707/job/137131089366 .

### Remaining CNUBUS identity blocker

After the dropdown compatibility fix, `StationIdentityTest` actually executes and
fails: 23 station entries have only 21 distinct names. `도서관앞` and `골프장`
each occur twice in `core/data/.../model/Location.kt`. `RootViewModel` selects
favorites by station name and `RootScreen` also uses the name as the lazy-list
key. The data, favorite selection and identity test are unchanged by this repair
(their blobs match the develop baseline). The test must remain enabled.

The prior six-app run passed Arducon 61 tests, Comssa 32, MyGrade 38 and Nanda 7;
CNUBUS passed 12 of 13 and MySenior's unit-test task is `NO-SOURCE`. It also
reported a CNUBUS `packageDebug` worker failure without an underlying exception
in the default log; the validation command now includes `--stacktrace` so any
persistent packaging failure is diagnosable on the final CI head.
Evidence: https://github.com/keelim/all/actions/runs/44997771441/job/137132410215 .

Direct develop push is withheld while an actual consumer test is failing.
Resolve canonical station/route-occurrence identity separately while preserving
stored favorite names and route selection behavior, then rerun the clean six-app
validation and recheck the remote develop SHA before a normal fast-forward push.
Do not rename/delete duplicate entries, weaken the assertion or migrate persisted
keys as part of this common-build repair.

## CNUBUS duplicate row-key repair verification (2026-11-17)

The station catalog has 23 rows but only 21 distinct names. `도서관앞` repeats
even at identical coordinates, with different route membership; `골프장` also
repeats. The name-only lazy key from #2284 is invalid. The follow-up uses the
immutable latitude/longitude bit patterns and all four route positions as a
presentation-only row key. Existing name-group favorites and DataStore keys
remain unchanged; no data or duplicate rows are removed.

The standard `:app-cnubus:testDebugUnitTest` run remains blocked by the existing
`appfunctions-service:1.0.0-rc01` resolution failure (JitPack HTTP 403). With the
service/compiler pair temporarily pinned to alpha09 for diagnosis and synthetic
local provider/Firebase fixtures, all 20 CNUBUS tests passed, including duplicate
keys, search filtering, restored favorites, removal/re-add and queued toggles.
The version catalog was restored byte-for-byte to rc01 before committing.
Logs: `/tmp/cnubus-identity-rc01.log`, `/tmp/cnubus-identity-alpha09.log`
(session-local). This does not establish rc01 compatibility, real Firebase
integration, device rendering or on-disk DataStore restore behavior.

## Dependency validation baseline (2026-11-22)

Fresh non-deployment CI with the checked-in Temurin 17.0.20+8 and Gradle 9.9.2 stopped during `:app-arducon` configuration because `local.defaults.properties` is absent from a clean checkout. Run: https://github.com/keelim/all/actions/runs/44985733013 . The validation draft now explicitly copies CI-only public test/empty provider defaults, leaving production configuration and secrets untouched. AppFunctions service resolution and both Arducon/Nanda Dependency Guard remain enabled; no downstream success is claimed until those tasks execute.

## Current dependency PR baseline and migration proposals (2026-11-22)

Source head `1c173d13b97c686d545a1f4eaf79fc51f16caa04` includes current develop
`da8f736e8257f9ae9db92329dafbe581b3408e23`. Its non-deployment CI independently
executes library build/tests, AppFunctions/app consumers, and Nanda Guard:
https://github.com/keelim/all/actions/runs/44988946234

- `:core:common-android:checkDebugAarMetadata` fails because dependencies require
  API 37.2+ while the shared convention uses android-37.0. Both Android convention
  paths explicitly set `minorApiLevel = 0`. Review SDK 37.2 installation and the
  shared convention together; do not bypass AAR checks or downgrade dependencies.
- `:feature:app-function:debugCompileClasspath` cannot resolve
  `androidx.appfunctions:appfunctions-service:1.0.0`. Its Google Maven POM returns
  404, while `appfunctions:1.0.0` and `appfunctions-compiler:1.0.0` return 200.
  A separate functional migration must replace the service artifact, adopt
  AppFunctionServiceEntryPoint and current declaration annotations, remove the
  obsolete AppFunctionContext/aggregation-option usage, and validate health/JSON
  tests and service/manifest behavior. The official API change record is
  https://developer.android.com/jetpack/androidx/releases/appfunctions .
- `:app-nanda:dependencyGuard` independently fails on releaseRuntimeClasspath
  drift. The accepted DataStore coordinates in #2299 do not account for existing
  unrelated drift. Compare fully resolved base/head graphs, review unrelated
  differences separately, update only approved baselines, and rerun Guard. No
  whole-repository rebaseline or skipped Guard check is acceptable evidence.

During verification Renovate independently merged out-of-scope #2311 and #2312;
they were not modified by this task. Validation was updated to current develop
before these results were recorded. The earlier missing defaults file is handled
explicitly by CI-only public test/empty configuration, not production secrets.

The root and build-logic wrappers also have different SHA-256 values for their
shared Gradle 9.9.2 URL (`971fcd...` versus `21bd5d...`). The root wrapper download
passed its checksum validation in CI; reconcile the nested wrapper separately.
The eight original Android dependency PRs remain unmerged while these baseline
failures block actual build/test verification. This entry is documentation only
and does not change the source or workflow evaluated in the linked CI run.

## Dependency validation after the common Android repair

The dependency-validation workflow uses SDK 37.2, the repository-pinned Java 17
build JDK, Java 21 for Robolectric, and the common offline fixture generator.
The earlier runs describe their linked commits. The GitHub runner resolved
both release runtime graphs, including Arducon's JitPack chart and its
submodules, in run 45021533229. The two release runtime baselines now record
those verified graphs. The Nanda baseline changes 176 additions and 138
removals. The Arducon baseline changes 344 additions and 342 removals.

## Common KMP migration verification (2026-11-22 KST)

Before stacking on the Device KMP branch, the changed-module `testDebugUnitTest` run with `--no-configuration-cache --continue`
passed all 13 `:feature:ui-setting` tests. `:feature:ui-scheme` compiled and its
unit-test task reported `NO-SOURCE`. The `:core:common` Android target compiled
Kotlin and Hilt-generated Java as transitives of the permitted tasks. Nanda and
MySenior also compiled their Hilt components and merged the common manifest.

The six app unit-test tasks did not run. Arducon, CNUBUS, Comssa, and MyGrade
lack local `google-services.json` files and could not resolve the existing
`androidx.appfunctions:appfunctions-service:1.0.0` dependency. Arducon also could
not resolve `com.github.tehras:charts:0.2.4-alpha`. JitPack returned HTTP 403 for
these artifacts. Nanda and MySenior use compile SDK 37.0. Resolved Compose
1.13.0 artifacts require 37.1, and MySenior's DeepLinkDispatch 7.3.1 requires
37.2. Session-local log: `/tmp/keelim-common-kmp-unit-tests-retry.log`.

The KMP Android plugin exposes `testAndroidHostTest` for `:core:common`. The
required `testDebugUnitTest` task is unavailable for this module and the
included build logic. The Android host tests and Desktop and Wasm compilation
remain unverified under the changed-module test policy.

After stacking on Device KMP, the same module-scoped run used SDK 37.2 and the
repository's public offline fixtures. CNUBUS, Comssa, MyGrade, Nanda, and
`:feature:ui-setting` passed 110 tests in total. MySenior and `:feature:ui-scheme`
reported `NO-SOURCE`. Android Kotlin and Hilt-generated Java compiled for both
KMP libraries. Arducon remains blocked locally by the JitPack HTTP 403 for the
existing chart dependency. Session-local log: `/tmp/keelim-common-stack-unit-tests.log`.
