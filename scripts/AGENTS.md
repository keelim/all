# :scripts

## Focus

`scripts/` owns the repository-level Python automation, lightweight build verification, dependency graph generation, cache maintenance, FCM notification dispatch, native Rust Android build helpers, and Google Lightbuild preview workflow structured as a standard `uv` package project (`scripts/pyproject.toml`, `scripts/uv.lock`, `src/all_scripts/`, and `tests/`).

## Project Layout

```text
scripts/
├── pyproject.toml              # uv project declaration, entrypoints, and dev dependencies
├── uv.lock                     # Locked dependency graph
├── .python-version             # Target Python version pin (3.13)
├── README.md                   # Package overview and usage guide
├── AGENTS.md                   # Operational boundary and agent rules
├── src/
│   └── all_scripts/
│       ├── __init__.py         # Package root
│       ├── cli.py              # Main dispatcher CLI (`all-scripts`)
│       ├── android_light.py    # Scoped Android CLI + Gradle workflow (`android-light`)
│       ├── compile_check.py    # Compile check for 5 core apps (`compile-check`)
│       ├── dependency_tree_update.py # Baseline update for 5 core apps (`dependency-tree-update`)
│       ├── fcm_admin.py        # FCM messaging (`fcm-admin`)
│       ├── gradle_cache.py     # Gradle cache cleanup (`gradle-cache`)
│       ├── gradle_dependencies_graph.py # Module dependency graph (`gradle-dependencies-graph`)
│       ├── gradle_profile.py   # Benchmark profiling (`gradle-profile`)
│       ├── hello_world.py      # Escaped message utility (`hello-world`)
│       ├── lightbuild_preview.py # Lightbuild preview workflow (`lightbuild-preview`)
│       └── rust_build.py       # Rust ABI target builder (`rust-build`)
├── tests/
│   ├── __init__.py
│   └── test_module_dependency_graph.py # Module dependency graph pytest/unittest suite
└── (compatibility wrappers at scripts/*.py forwarding to src/all_scripts)
```

## CLI Usage

From within `scripts/`:
```bash
uv run all-scripts --help
uv run android-light doctor
uv run compile-check
uv run pytest
```

From monorepo root:
```bash
uv run --project scripts all-scripts --help
uv run --project scripts android-light doctor
python3 scripts/hello_world.py
```

## Boundaries & Operational Rules

- Standard packaging is enforced via `hatchling` in `scripts/pyproject.toml`.
- Default runtime dependencies remain empty (`dependencies = []`) so build and verification run offline; external libraries (e.g. `firebase-admin`) are isolated under optional extras (`--extra fcm`).
- Never create a `bin/` directory anywhere in the monorepo (`test ! -d bin`) and keep virtual environments (`.venv/`) ignored in version control.
- Preserve existing Gradle Wrapper, SDK, and signing configurations; scripts must not auto-accept Android SDK licenses or expose secret material.
