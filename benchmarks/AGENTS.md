# :benchmarks

## Focus

JVM benchmark fixtures live in `src/main/java/com/keelim/benchmarks/`. Keep benchmarks deterministic and isolate the operation being measured.

## Boundaries

- Keep production code out of benchmark fixtures; extract reusable code to its owning module first.
- Add benchmark inputs and setup explicitly so the measured work is visible.
