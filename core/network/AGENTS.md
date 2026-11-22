# :core:network

## Focus

Network clients and DI are in `di/`; HTTP behavior is in `interceptor/`; API payloads are in `model/`.

## Boundaries

- Keep credentials in the configured secrets mechanism, never in client code or resources.
- Centralize retries, caching, and client policy in their existing interceptor or DI boundary.
