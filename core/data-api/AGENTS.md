# :core:data-api

## Focus

Repository contracts are in `repository/`; transport-facing models and JSON contracts are in `model/` and `json/`.

## Boundaries

- Keep contracts implementation-free and platform-neutral.
- Evolve a repository interface and its `:core:data` implementation together; preserve callers or migrate them in the same change.
