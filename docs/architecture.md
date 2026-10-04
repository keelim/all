# Android and multiplatform architecture

Six Android application modules compose shared libraries and independently own app navigation and release workflows. `composeApp` is a separate Compose Multiplatform host. The Gradle module registry and included `build-logic` build determine build membership; the Rust crate, iOS host, and TypeSpec package have separate manifests.

UI events flow from a Screen through its Route/host callbacks and ViewModel. ViewModels expose StateFlow/Flow; repositories coordinate persistence/network data and return state to lifecycle-aware Compose collectors. App features consume repository contracts rather than constructing DAOs.

`core:data-api` supplies repository interfaces; `core:data` implements them using Room/DAOs in `shared` and bindings/mapping in `core:database`, DataStore, and network clients. `core:model` carries shared models, `core:domain` holds deterministic wellness rules, and `core:common` holds platform-neutral helpers. Android lifecycle, permissions, and SDK integrations belong to `common-android` and `device-android`. Network policy stays in the network DI/interceptor boundary.

`core:designsystem` supplies canonical Android Compose tokens/themes/primitives; `core:component` delegates compatibility APIs and hosts larger reusable widgets. Shared resource identifiers and serializable routes connect apps and feature modules. Settings modules own individual routes/state while the settings hub coordinates navigation.

`shared` uses common and platform source sets for models, Room, and platform abstractions. Desktop window code is used only in desktop Compose contexts. `widget`, `catalog`, and `benchmarks` provide Glance integration, component demonstrations, and isolated performance fixtures. They do not own product rules.

Firebase services, Google Maps, ads, and HTTP providers sit behind their app/device/network boundaries. TypeSpec describes API schema; the Rust cdylib provides an experimental native surface and the Xcode host supplies an Apple shell. Their presence is not proof of end-to-end integration.
