# Android maintenance traps

The application named app-nanda contains wellness and health-tool surfaces; an old diagnosis-only description is not a current product inventory. Inspect real entry screens before moving or removing features.

`core:component` still exposes compatibility theme APIs. Replacing a call is incomplete if it bypasses canonical `core:designsystem` tokens or changes behavior. Inspect callers and preserve explicit Text styling and animated state changes.

Module presence does not imply a `testDebugUnitTest` task: pure JVM, multiplatform, native, schema, and iOS units can lack it. The allowed response is to report that missing verification, not choose a convenient broader build.

The docs directory has historically been ignored. Required operational documents need exact Git exceptions; do not unignore all local plans, schemas' generated output, or node_modules to make a guide visible.

Desktop source in `shared` is not an Android entry point. Source-set placement matters when resolving platform APIs; keep common code free of platform-only imports and verify the intended target before claiming cross-platform readiness.

## Baseline Profile provenance

CNUBUS, Comssa and MySenior previously shipped the same copied Now in Android
profile (15,027 lines, including 1,893 sample-package references and no Keelim
package references). Those app-owned files have been removed. Dependency-provided
profiles remain available; MyGrade's app-specific profile is retained. Removal is
provenance cleanup, not a measured startup improvement.

Generate replacements from each application's current launch and user journeys
(`journeys/app-*-journey.xml`) with an AndroidX Baseline Profile producer. There is
currently no producer module in this repository; do not invent a generation task
or copy a different application's profile. Keep generated files with the target
package, source commit, device/API and scenario provenance in the accompanying PR.
Compare a non-debuggable, minified target on a physical device with Macrobenchmark
`CompilationMode.None` and `CompilationMode.Partial(BaselineProfileMode.Require)`
using startup and frame metrics. CLI system traces can help diagnose the path;
they do not generate Baseline Profiles. No replacement profiles or performance
measurements are claimed by this cleanup.

Run `python3 scripts/check_baseline_profiles.py` to reject reintroduced sample
profiles before committing. Official guidance:
https://developer.android.com/topic/performance/baselineprofiles/create-baselineprofile
https://developer.android.com/topic/performance/baselineprofiles/measure-baselineprofile
