# :core:datastore-proto

## Focus

The persisted user-settings contract is `src/main/proto/com/keelim/core/data/user_setting.proto`.

## Boundaries

- Make protobuf changes backward-compatible: retain field numbers and reserve removed numbers/names.
- Treat generated sources as build output; edit only the `.proto` contract.
