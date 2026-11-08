#!/usr/bin/env python3
"""Emit a single-line escaped message payload for Slack workflow verification."""

from __future__ import annotations


def main(argv: list[str] | None = None) -> int:
    result = "Hello World"
    print(result.replace("\n", "\\n"))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
