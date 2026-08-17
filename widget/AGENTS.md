# :widget

Before changing this module, read the [repository rules](../AGENTS.md).

## Focus

Glance widget entry points are `ui/WidgetReceiver.kt` and `ui/Widget.kt`; widget metadata is in `src/main/res/xml*/widget_info.xml`.

## Boundaries

- Keep receiver declarations and widget metadata synchronized.
- Treat widget updates as background-safe: obtain data through supported asynchronous paths, never on the main thread.
