#!/usr/bin/env python3
"""Send Firebase Cloud Messages to a target device token or topic."""

from __future__ import annotations

import argparse
import sys


def initialize_firebase(cred_path: str):
    try:
        import firebase_admin
        from firebase_admin import credentials
    except ImportError as exc:
        print(
            "Error: firebase-admin package is required. Run with: uv run --project scripts --extra fcm fcm-admin ...",
            file=sys.stderr,
        )
        raise SystemExit(1) from exc

    try:
        cred = credentials.Certificate(cred_path)
        firebase_admin.initialize_app(cred)
        print("Firebase Admin SDK initialized successfully.")
    except Exception as exc:
        print(f"Error initializing Firebase Admin SDK: {exc}", file=sys.stderr)
        raise SystemExit(1) from exc


def send_to_token(
    token: str, title: str, body: str, data: dict[str, str] | None = None
) -> None:
    from firebase_admin import messaging

    message = messaging.Message(
        notification=messaging.Notification(
            title=title,
            body=body,
        ),
        data=data if data else {},
        token=token,
    )
    try:
        response = messaging.send(message)
        print(f"Successfully sent message to token: {response}")
    except Exception as exc:
        print(f"Error sending message to token: {exc}", file=sys.stderr)


def send_to_topic(
    topic: str, title: str, body: str, data: dict[str, str] | None = None
) -> None:
    from firebase_admin import messaging

    message = messaging.Message(
        notification=messaging.Notification(
            title=title,
            body=body,
        ),
        data=data if data else {},
        topic=topic,
    )
    try:
        response = messaging.send(message)
        print(f"Successfully sent message to topic: {response}")
    except Exception as exc:
        print(f"Error sending message to topic: {exc}", file=sys.stderr)


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description="Send Firebase Cloud Messages.")
    parser.add_argument("--cred", required=True, help="Path to serviceAccountKey.json")
    parser.add_argument("--token", help="Target device token")
    parser.add_argument("--topic", help="Target topic")
    parser.add_argument("--title", required=True, help="Notification title")
    parser.add_argument("--body", required=True, help="Notification body")
    parser.add_argument(
        "--data",
        nargs="*",
        help="Key=Value data pairs (e.g. key1=value1 key2=value2)",
    )

    args = parser.parse_args(argv)

    if not args.token and not args.topic:
        print("Error: Either --token or --topic must be provided.", file=sys.stderr)
        return 1

    data: dict[str, str] = {}
    if args.data:
        for item in args.data:
            key, value = item.split("=", 1)
            data[key] = value

    initialize_firebase(args.cred)

    if args.token:
        send_to_token(args.token, args.title, args.body, data)
    elif args.topic:
        send_to_topic(args.topic, args.title, args.body, data)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
