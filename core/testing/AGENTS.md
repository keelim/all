# :core:testing

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Shared test support is under `platform/` and `util/`, including `MainDispatcherRule` and platform fakes.

## Boundaries

- Keep helpers small, deterministic, and reusable across modules.
- Add a shared helper only after at least two independent tests need the same setup.
