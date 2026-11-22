# all-scripts

Unified Python automation, build, and verification package for the `all` monorepo, managed via `uv`.

## Installation & Environment

This package is managed with `uv`.

```bash
# Run via uv without manual installation
uv run --project scripts all-scripts --help
uv run --project scripts android-light doctor
```

## CLI Commands

- `all-scripts`: Dispatcher CLI for all subcommands
- `android-light`: Scoped Android CLI + Gradle Wrapper workflow
- `compile-check`: Run `:compileDebugSources` across core apps
- `dependency-tree-update`: Run `:dependencyGuardBaseline` across core apps
- `fcm-admin`: Send FCM notifications to device token or topic
- `gradle-cache`: Prune old Gradle cache files
- `gradle-dependencies-graph`: Generate repository module dependency graph
- `gradle-profile`: Run `gradle-profiler` benchmarks
- `hello-world`: Output escaped string for CI workflows
- `lightbuild-preview`: Control Google Lightbuild preview project
- `rust-build`: Build `all-rust-lib` release binaries across Android ABIs

## Headless Compose review

Use the existing official Android CLI without installing or updating it:

```sh
uv run --project scripts compose-preview list --target a2ui
uv run --project scripts compose-preview render --target a2ui --fqn '<name from list>' --daemon
# Existing binary outside PATH:
uv run --project scripts compose-preview list --cli /path/to/android-cli
```

Targets are limited to `a2ui`, `comssa`, and `cnubus`. Render exports PNG plus
component-tree JSON into a temporary directory unless `--output` is provided.
Use `--no-image` for tree-only review. `--daemon` is opt-in and reuses the CLI
renderer during iterative review; its default idle timeout remains CLI-owned.
The wrapper never installs tools, accepts licenses, changes signing, forwards
arbitrary Gradle tasks, or selects a release/publish target. CLI failures are
returned to the caller. The first preview still needs a working Gradle build,
SDK and existing local configuration; no device or Android Studio is required.

Inspect generated PNGs and tree semantics/bounds before accepting a UI change.
Preview rendering is visual feedback, not an Android runtime performance test or
a replacement for app unit tests and device Journeys. This Gradle workflow is
independent of `previews/lightbuild`; it does not migrate the product build graph.
Official reference: https://developer.android.com/tools/agents/android-cli/commands/preview_render
