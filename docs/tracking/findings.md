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
