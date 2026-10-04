# Device and data boundaries

Protect local wellness/study/link records, notification data, provider credentials, signing material, and device permissions. The repository does not define one shared account/role system for all apps; provider/session behavior remains app-specific.

| Actor/action | Required condition | Denied boundary |
| --- | --- | --- |
| App uses a protected Android capability | Correct manifest declaration and granted runtime permission where required | A denied permission is not a successful operation |
| UI reads/writes stored records | ViewModel/repository contract | Direct DAO access or main-thread database/network work |
| App launches external content | Validated intent/URL at activity/navigation boundary | Unvalidated input treated as trusted content |
| Maintainer changes provider/signing configuration | Explicit owner scope and safe local configuration | Secrets in client source, resources, logs, or fixtures |

Keep network credentials in the existing secrets mechanism; local properties and Firebase files remain ignored. Provider authentication failure or missing configuration leaves the corresponding feature unverified. Do not fabricate successful provider data. Credential issue, expiry, rotation, and revocation are handled by the relevant provider/operator; no unified lifecycle policy is established here.

Centralize WebView URL handling/settings and grant only capabilities required by the screen. Parameterize SQL instead of concatenating input. Report exposed signing/provider material, unauthorized data access, or destructive schema changes immediately. Record the affected module and observed failure without private record contents. There is no verified repository-wide security audit backend.
