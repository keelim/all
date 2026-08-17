# :core:device-android

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Device integrations live in `initialize/`, `receiver/`, `ui/sms/`, `util/`, and `di/`.

## Boundaries

- Keep Android permissions, broadcasts, and SDK integrations isolated in this module.
- Match manifest declarations to receiver and integration behavior.
