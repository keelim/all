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
