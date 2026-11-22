# Canonical tokens with compatibility widgets

## Context

Apps share visual behavior while existing callers still consume component APIs.

## Decision and alternatives

Keep tokens/themes/primitives in core:designsystem and delegate compatibility/higher widgets from core:component instead of duplicating token systems.

## Consequences

UI reorganization preserves domain behavior and existing consumer APIs or migrates callers together.
