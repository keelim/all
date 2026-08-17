# :core:model

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Shared domain models are in `src/main/java/com/keelim/model/`, including finance, alarms, notifications, study, and health data.

## Boundaries

- Keep models platform-neutral and serialization-compatible.
- Coordinate breaking model changes with every persisted, network, and UI consumer.
