# Product meaning and preserved behavior

DeepLink Tester owns development utilities and test-link history; CNUBUS owns campus bus/map presentation; MyGrade owns grade estimates and study records; Buddy Daily owns wellness records and related health tools; Comssa owns financial utilities; MySenior is a minimal senior-oriented host. Shared implementation does not make their product identities or persisted records interchangeable.

A design-system change may reorganize presentation but must preserve calculations, domain rules, and data. Organizing the DeepLink Tester home catalog does not require changing link models, repository contracts, or database schema. Calculators remain deterministic for the same input; changing formulas is a product change, not styling cleanup.

User-facing dates use local system time with `yyyy.MM.dd`, `yyyy.MM.dd HH:mm`, or 24-hour `HH:mm`. Storage and API values remain ISO/epoch. Currency displays follow locale and an explicit currency code when the domain provides one; formatting must not change stored amounts or replace unavailable values with invented data.

Current wellness behavior distinguishes input validation from insight generation. Daily sleep/stress/energy/desire/confidence values are each 1–5. Seven-day completion covers today and the preceding six dates, starts no earlier than the routine's creation date, and returns zero eligible/completed days for a future creation date. A completed date outside that eligible window does not count twice or extend it.

Goal progress is 1 when baseline equals target or the current value has reached/passed the target in its intended direction. Otherwise it is `1 - abs(target-current)/abs(target-baseline)`, clamped to 0–1. These are current implemented calculation contracts, not claims of clinical efficacy or a new product policy.

Invalid or incomplete deep-link input is visible as an invalid state; it is not silently treated as a valid launch. Missing device capability is represented explicitly. Preserve existing thin/experimental hosts without presenting them as fully released applications.
