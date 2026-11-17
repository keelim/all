"""Reject copied sample profiles; this is a provenance guard, not profile validation."""
from pathlib import Path


def check(root: Path) -> list[Path]:
    return [
        path for path in sorted(root.glob("app-*/src/main/*-prof.txt"))
        if "com/google/samples/apps/nowinandroid" in path.read_text(encoding="utf-8")
    ]


def main() -> int:
    root = Path(__file__).resolve().parents[3]
    invalid = check(root)
    for path in invalid:
        print(f"Copied Now in Android profile: {path.relative_to(root)}")
    if not invalid:
        print("No copied Now in Android app profiles found; device validation is separate.")
    return 1 if invalid else 0
