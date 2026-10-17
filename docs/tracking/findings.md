# Unresolved findings

Native Rust loading, Xcode linkage, and Compose platform execution were not established by the inspected manifests. A source host or declared target is not proof of a working end-to-end build. These require explicit platform verification scope and prerequisites before runtime claims.

Some modules do not expose the repository's permitted testDebugUnitTest task. That is a verification limitation, not permission to run an alternate build. Existing local product changes remain outside documentation edits. Specifically:
- `:core:navigation`: pure JVM library (`keelim.jvm.library`) configured with standard Kotlin/JVM plugins; does not register the Android `testDebugUnitTest` variant task.
- `:core:component`: Kotlin Multiplatform library (`keelim.multiplatform`); does not expose `testDebugUnitTest`.
- `:build-logic`: composite build (`includeBuild("build-logic")`) convention plugins; not a root subproject with `testDebugUnitTest`.
Per repository hard gates (`AGENTS.md`, `docs/standards.md`, `docs/operations.md`), running substitute tasks (such as `test`, `desktopTest`, `assembleDebug`, `lint`, or root `test`) for these modules is strictly prohibited.
