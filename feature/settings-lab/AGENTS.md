# :feature:settings-lab

## Focus

Experimental settings behavior is defined by `LabRoute.kt`, `LabUiState.kt`, `LabViewModel.kt`, and `SettingsLabNavigation.kt`.

## Boundaries

- Keep experimental UI behind its route and ViewModel boundary.
- Model asynchronous states explicitly so pending and failed experiments remain visible to users.
