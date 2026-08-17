# :feature:settings-device

Before changing this module, read the [repository rules](../../AGENTS.md).

## Focus

Device information UI and state live in `screen/device/`; bindings are in `di/DeviceInfoModule.kt`.

## Boundaries

- Keep platform data acquisition behind injected dependencies.
- Render unavailable device data deliberately rather than assuming every capability exists.
