# Android maintenance traps

The application named app-nanda contains wellness and health-tool surfaces; an old diagnosis-only description is not a current product inventory. Inspect real entry screens before moving or removing features.

`core:component` still exposes compatibility theme APIs. Replacing a call is incomplete if it bypasses canonical `core:designsystem` tokens or changes behavior. Inspect callers and preserve explicit Text styling and animated state changes.

Module presence does not imply a `testDebugUnitTest` task: pure JVM, multiplatform, native, schema, and iOS units can lack it. The allowed response is to report that missing verification, not choose a convenient broader build.

The docs directory has historically been ignored. Required operational documents need exact Git exceptions; do not unignore all local plans, schemas' generated output, or node_modules to make a guide visible.

Desktop source in `shared` is not an Android entry point. Source-set placement matters when resolving platform APIs; keep common code free of platform-only imports and verify the intended target before claiming cross-platform readiness.
