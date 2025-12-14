
import firebase_admin
from firebase_admin import credentials, messaging
import argparse
import sys

def initialize_firebase(cred_path):
    try:
        cred = credentials.Certificate(cred_path)
        firebase_admin.initialize_app(cred)
        print("Firebase Admin SDK initialized successfully.")
    except Exception as e:
        print(f"Error initializing Firebase Admin SDK: {e}")
        sys.exit(1)

def send_to_token(token, title, body, data=None):
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
    except Exception as e:
        print(f"Error sending message to token: {e}")

def send_to_topic(topic, title, body, data=None):
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
    except Exception as e:
        print(f"Error sending message to topic: {e}")

def main():
    parser = argparse.ArgumentParser(description="Send Firebase Cloud Messages.")
    parser.add_argument("--cred", required=True, help="Path to serviceAccountKey.json")
    parser.add_argument("--token", help="Target device token")
    parser.add_argument("--topic", help="Target topic")
    parser.add_argument("--title", required=True, help="Notification title")
    parser.add_argument("--body", required=True, help="Notification body")
    parser.add_argument("--data", nargs='*', help="Key=Value data pairs (e.g. key1=value1 key2=value2)")

    args = parser.parse_args()

    if not args.token and not args.topic:
        print("Error: Either --token or --topic must be provided.")
        sys.exit(1)

    data = {}
    if args.data:
        for item in args.data:
            key, value = item.split('=', 1)
            data[key] = value

    initialize_firebase(args.cred)

    if args.token:
        send_to_token(args.token, args.title, args.body, data)
    elif args.topic:
        send_to_topic(args.topic, args.title, args.body, data)

if __name__ == "__main__":
    main()
