# Android maintenance traps

The application named app-nanda contains wellness and health-tool surfaces; an old diagnosis-only description is not a current product inventory. Inspect real entry screens before moving or removing features.

`core:component` still exposes compatibility theme APIs. Replacing a call is incomplete if it bypasses canonical `core:designsystem` tokens or changes behavior. Inspect callers and preserve explicit Text styling and animated state changes.

Module presence does not imply a `testDebugUnitTest` task: pure JVM, multiplatform, native, schema, and iOS units can lack it. The allowed response is to report that missing verification, not choose a convenient broader build.

The docs directory has historically been ignored. Required operational documents need exact Git exceptions; do not unignore all local plans, schemas' generated output, or node_modules to make a guide visible.

Desktop source in `shared` is not an Android entry point. Source-set placement matters when resolving platform APIs; keep common code free of platform-only imports and verify the intended target before claiming cross-platform readiness.

## Serialization shrinking

App rules rely on the `kotlinx-serialization-core-jvm:1.11.0` bundled consumer
rules for serializer/companion lookup, objects and runtime annotations, rather
than keeping every member of every `@Serializable` class. Retain the optimized
Android default rules and all unrelated warning rules. Explicit app reflection
entry points may still need narrow rules; audit new reflection consumers before
adding them. Validate a minified APK's JSON/polymorphic serialization and typed
navigation on device before release. Debug unit tests do not exercise R8.

Use `android analyze --analysis=optimization --format=json <apk>` on the same
release variant before and after a rule change. The current AGP 9.2.1 does not
provide the AGP 9.3+ standalone `analyzeReleaseR8Config` path. No temporary R8
version override or release/deploy workflow is necessary for this rule cleanup.
