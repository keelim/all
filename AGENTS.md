# all working guide

A multi-app Android and Compose Multiplatform repository for six apps and shared libraries, using Compose, Hilt, Room/DataStore, and convention-plugin Gradle. App workflows and release boundaries remain distinct.

## Project structure

```text
./
├── AGENTS.md → Working guide
├── CLAUDE.md → Same guide for Claude
├── docs/
│   ├── architecture.md → Components and data flow
│   ├── business-rules.md → Domain meaning and edge cases
│   ├── security.md → Access, credentials, and protected data
│   ├── standards.md → Change and verification rules
│   ├── engineering-notes.md → Operational traps
│   ├── operations.md → Setup and scoped commands
│   ├── contracts.md → Consumer inputs, outputs, and failures
│   ├── tracking/
│   │   ├── status.md → Capabilities and verified scope
│   │   ├── findings.md → Unresolved issues
│   │   └── decisions/
│   │       ├── index.md → Decision index
│   │       ├── 0001-shared-ui-boundary.md → Canonical tokens with compatibility widgets
│   │       └── 0002-focused-module-checks.md → Changed-module unit tests only
│   └── AGENTS.md → API schema package and technical material boundary
├── app-nanda/
│   └── AGENTS.md → app-nanda ownership and invariants
├── widget/
│   └── AGENTS.md → widget ownership and invariants
├── app-arducon/
│   └── AGENTS.md → app-arducon ownership and invariants
├── catalog/
│   └── AGENTS.md → catalog ownership and invariants
├── shared/
│   └── AGENTS.md → shared ownership and invariants
├── build-logic/
│   └── AGENTS.md → build-logic ownership and invariants
├── benchmarks/
│   └── AGENTS.md → benchmarks ownership and invariants
├── app-my-grade/
│   └── AGENTS.md → app-my-grade ownership and invariants
├── core/
│   ├── device-android/
│   │   └── AGENTS.md → device-android ownership and invariants
│   ├── database/
│   │   └── AGENTS.md → database ownership and invariants
│   ├── designsystem/
│   │   └── AGENTS.md → designsystem ownership and invariants
│   ├── network/
│   │   └── AGENTS.md → network ownership and invariants
│   ├── navigation/
│   │   └── AGENTS.md → navigation ownership and invariants
│   ├── component/
│   │   └── AGENTS.md → component ownership and invariants
│   ├── data-api/
│   │   └── AGENTS.md → data-api ownership and invariants
│   ├── testing/
│   │   └── AGENTS.md → testing ownership and invariants
│   ├── common/
│   │   └── AGENTS.md → common ownership and invariants
│   ├── model/
│   │   └── AGENTS.md → model ownership and invariants
│   ├── resource/
│   │   └── AGENTS.md → resource ownership and invariants
│   ├── data/
│   │   └── AGENTS.md → data ownership and invariants
│   ├── domain/
│   │   └── AGENTS.md → domain ownership and invariants
│   ├── datastore-proto/
│   │   └── AGENTS.md → datastore-proto ownership and invariants
│   └── common-android/
│       └── AGENTS.md → common-android ownership and invariants
├── feature/
│   ├── ui-web/
│   │   └── AGENTS.md → ui-web ownership and invariants
│   ├── ui-scheme/
│   │   └── AGENTS.md → ui-scheme ownership and invariants
│   ├── ui-setting/
│   │   └── AGENTS.md → ui-setting ownership and invariants
│   ├── settings-alarm/
│   │   └── AGENTS.md → settings-alarm ownership and invariants
│   ├── settings-notification/
│   │   └── AGENTS.md → settings-notification ownership and invariants
│   ├── settings-theme/
│   │   └── AGENTS.md → settings-theme ownership and invariants
│   ├── settings-core/
│   │   └── AGENTS.md → settings-core ownership and invariants
│   ├── settings-admin/
│   │   └── AGENTS.md → settings-admin ownership and invariants
│   ├── app-function/
│   │   └── AGENTS.md → app-function ownership and invariants
│   ├── settings-device/
│   │   └── AGENTS.md → settings-device ownership and invariants
│   └── settings-lab/
│       └── AGENTS.md → settings-lab ownership and invariants
├── app-cnubus/
│   └── AGENTS.md → CNUBUS boundary
├── app-comssa/
│   └── AGENTS.md → Comssa boundary
├── app-mysenior/
│   └── AGENTS.md → MySenior boundary
├── composeApp/
│   └── AGENTS.md → Compose application host boundary
├── all-rust-lib/
│   └── AGENTS.md → Native Rust boundary boundary
└── allIos/
    └── AGENTS.md → Apple host boundary
```

## Hard gates

1. Preserve product calculations and persisted records when changing shared UI or module boundaries.
2. Keep canonical Android tokens/primitives in core:designsystem and compatibility widgets in core:component.
3. Features use repository contracts; no main-thread I/O, direct DAO access, or secrets in client source.
4. Validate only changed modules with testDebugUnitTest; never substitute builds, lint, connected tests, root test, or git diff --check without an explicit request.

## Before work

Read `docs/standards.md`, `docs/engineering-notes.md`, and the closest module guide.

Before module/dependency work inspect `settings.gradle.kts`, `gradle/libs.versions.toml`, and `build-logic/AGENTS.md`. Before schema changes inspect the owning `shared`/database/protobuf guide. Before app work inspect its current entry screens, not an old feature list. Design-system changes require both provider and consumer guides; source discovery uses the owning all index.

## Problem routing

Immediately report exposed signing/provider credentials, loss of user records, or a schema/navigation change that crosses app ownership without authorization. Record other unresolved issues and unavailable allowed test tasks in `docs/tracking/findings.md`.
