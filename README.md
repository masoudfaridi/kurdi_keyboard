# Kurdi Keyboard 2.0

Based on the 2017 version `BACKUP3/2017.12.13.01` (v1.5.6).

## What's new
- **Word prediction** for Kurdish (Sorani), Persian and English, shown in a bar above the keys (where the tools row is). The keyboard learns the words you type, and suggests your next word. Everything it learns stays on the phone.
- **Swipe typing**: put your finger on the first letter and slide over the letters of the word without lifting. Other guesses appear in the bar; tap one to replace the word.
- Settings → "پیش‌بینی کلمات و تایپ سوایپی": turn prediction, swipe and auto-space on or off, or clear the learned words.

## Themes
The 2017 bitmap themes are gone. `KeyboardTheme.java` holds 24 themes, each just a palette and a key style, drawn in code (`KKeyboardView.onDraw`). Old theme choices map to the closest new theme automatically. Pick one in Settings -> Theme (live previews, `ThemePickerActivity`). To add a theme, add one `add(...)` line.

## Bugs fixed
- Builds with today's Android Studio (Gradle 9.1 / AGP 8.13 / SDK 35, minSdk 21).
- Clipboard history and ready sentences failed on Android 10+ (they wrote to `/sdcard/data/data/...`). They are now stored in the app's private storage.
- Clipboard history: random order, an extra line break on each pasted item, and a crash when the folder was missing.
- Clipboard watching no longer starts a background service (not allowed since Android 8). The keyboard listens itself.
- Ready-sentences dialog used `TYPE_SYSTEM_ALERT` and crashed on Android 8+.
- Crashes when the app you were typing in closed mid-keystroke (null `InputConnection`).
- Phone contacts: asks for the runtime permission instead of crashing.
- `android:exported`, storage permissions and the manifest updated for Android 12+.

## Build
```
gradlew assembleDebug        # app/build/outputs/apk/debug/app-debug.apk
gradlew testDebugUnitTest    # prediction + swipe tests
```
Dictionaries: `app/src/main/assets/dict/*.dict`, built from the kafe_keyboard word lists by `tools/build_dicts.py` (licences in `assets/dict/NOTICE.txt`, CC BY-SA 4.0).
