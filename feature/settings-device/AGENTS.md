# :feature:settings-device

## Focus

Device information UI and state live in `screen/device/`; bindings are in `di/DeviceInfoModule.kt`.

## Boundaries

- Keep platform data acquisition behind injected dependencies.
- Render unavailable device data deliberately rather than assuming every capability exists.
