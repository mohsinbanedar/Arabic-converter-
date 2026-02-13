# Arabic Listener (Android MVP)

This project is an Android app starter that:

1. Uses an `AccessibilityService` to monitor visible text changes.
2. Detects Arabic script in real time.
3. Converts Arabic to **instant offline English + Hinglish note** using local dictionary/transliteration.
4. Optionally improves English translation with ML Kit when model is available.
5. Stores everything in local Room DB and shows it in the app UI.

## Build

- Open in Android Studio (Hedgehog+ recommended).
- Let Gradle sync and download dependencies.
- Run on an Android device (API 26+).

## Enable live detection

1. Open the app.
2. Tap **Open Accessibility Settings**.
3. Enable **Arabic Listener** accessibility service.

## Offline behavior

- App now works in offline mode for instant conversion.
- If ML Kit model is available, translation quality may improve automatically.
- If internet is unavailable, offline conversion still continues immediately.

- Hinglish output now returns only converted content (no extra explanatory sentence).


## Video demo (recording guide)

If you want to make a quick user demo video after installing the app, follow the ready script in:

- `VIDEO_USAGE_SCRIPT.md`
