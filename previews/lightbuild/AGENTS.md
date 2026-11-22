# Independent Lightbuild preview

This directory is a separate Google Lightbuild 0.0.30 project root co-managed in
the `all` Git repository. It is a new sample, not a port of CNUBUS or another app.

- Keep `project.lightbuild.yaml` here, never at the Gradle repository root.
- Do not include these modules in `settings.gradle.kts`, or reference Gradle/KMP
  modules from this project. Shared Git ownership does not create a shared build graph.
- The app module only assembles the APK; sources and host tests belong to `app-core`.
- Use `uv run --project scripts python scripts/lightbuild_preview.py query|build|test` from the repository root.
  `test` runs `app-core:hostTest` only. Device tests, installation, release signing,
  and publishing are separate work.
- Generate `resolved.deps` with Lightbuild; commit those files without hand editing.
- Pin this independent project's versions in YAML. Do not consume or change the
  root Gradle version catalog to configure this unsupported-catalog preview.
- Keep SDK paths, local configuration, caches, APKs, and reports out of commits.
- For an existing product change, return to its Gradle owner and module guide.
