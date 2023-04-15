# State

## Diagram

```mermaid
flowchart TB
    Empty --> Screen
    Screen --> Loading --> Success
    Screen --> Loading --> Fail
    Fail -->|Retry maximum 3 or 5| Loading
    Screen -->|Side Effect| Screen
    Success --> Next
    Fail --> Next
    Next --> Screen
```
