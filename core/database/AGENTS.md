# :core:database

## Focus

Hilt database bindings are in `DatabaseModule.kt` and `DaoModule.kt`; entity conversions are in `mapper/`.

## Boundaries

- Keep persistence mapping at this boundary and make schema-affecting changes compatible with `:shared` database ownership.
- Bind DAOs through DI rather than exposing direct construction to features.
