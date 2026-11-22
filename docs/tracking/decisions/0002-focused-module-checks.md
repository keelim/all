# Changed-module unit tests only

## Context

The existing repository policy deliberately bounds default validation to changed modules.

## Decision and alternatives

Use only each changed module’s testDebugUnitTest task; broad builds, lint, connected tests, and diff-check require explicit requests.

## Consequences

Modules without that task are reported as unverified; this policy does not prove release or platform readiness.
