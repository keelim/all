# Development environment

Use mise 2026.10.8 or newer and the checked-in `mise.toml` and `mise.lock`.
The development verification lane pins Eclipse Temurin **17.0.20+8**, matching
the existing runtime used for scoped module tests. This is the Gradle runtime;
the Java/Kotlin compilation target remains 17.

The existing Gradle Wrappers remain at **9.4.1**, Android Gradle Plugin at
**9.2.1**, Kotlin at **2.4.10**, and compile/target SDK at **37**. AGP 9.2
requires Gradle 9.4.1 and JDK 17 or newer, supports API 37, and uses Android
SDK Build Tools 36.0.0 by default. See the
[Android compatibility table](https://developer.android.com/build/releases/agp-9-2-0-release-notes)
and [Gradle Java compatibility table](https://docs.gradle.org/current/userguide/compatibility.html).

After reviewing and trusting this checkout's configuration:

```sh
mise trust mise.toml
mise install --locked
mise run doctor
```

The lockfile contains macOS ARM64 and Linux x64 JDK artifacts. Keep tool
upgrades separate from setup; do not use `mise upgrade` or regenerate locks
as a routine installation step. Do not install a separate Gradle version:
all commands use the repository's `./gradlew`.

Keep the existing Android SDK, `local.properties`, `local.defaults.properties`,
Firebase files, and signing/provider configuration. The checks do not install
SDK packages, accept licenses, create credentials, or modify these files.
Have the required SDK platform and Build Tools installed before Gradle checks.
The verification tasks pass `-Pandroid.builder.sdkDownload=false` so missing SDK
packages fail rather than being downloaded automatically.
`doctor` checks the JDK and Wrapper; a passing result does not certify SDK,
provider, signing, or release readiness.

## Representative verification

```sh
mise run test
mise run lint
mise run build
# Or run these checks in the same order as CI:
mise run ci
```

These tasks target `core:common-android`: its existing `testDebugUnitTest`,
`lintDebug`, and `assembleDebug` tasks. They cover a shared Android library,
not every app or all 37 registered modules. For an implementation change,
follow `AGENTS.md` and the affected module's guide and run its required tests.
The routine development checks do not sign, publish, or deploy anything.

Each verification task first runs `doctor`, which rejects a missing or different
JDK before starting module work. Task execution does not automatically install
missing mise tools. Run the
explicit setup command first. When this repository is nested in the maestro
workspace, mise also reads parent configuration; an enclosing setup can
therefore declare additional tools. The repository verification workflow runs
the same tasks from an independent checkout.

The new development verification workflow uses the locked Temurin JDK. Its
runner must already provide SDK platform 37 and Build Tools 36.0.0; the workflow
does not provision them or accept licenses.
Existing release/deployment workflows retain their Zulu 21 setup until their
runtime is evaluated separately. A development check is not a release build.

Gradle still needs its normal cache and dependency access. If the execution
environment denies a cache lock or filesystem operation, report that failure;
changing the mise pin does not fix an operating-system permission denial.
