# HFC Keyboard - Real-Time Android System Keyboard

A production-ready Android system keyboard that replaces typed letters with HFC cipher symbols in real-time.

## Features

- **Real-time typing**: Letters instantly converted to HFC symbols as you type
- **System-wide**: Works in WhatsApp, Chrome, Notes, and all apps
- **No conversion needed**: No decode button, no after-processing
- **Native Android**: Built with Kotlin using InputMethodService
- **Pure Native**: No Flutter, no webview - 100% native Android
- **HFC Cipher Mode**: Toggle on/off via settings
- **Keypress Feedback**: Sound and vibration options
- **Dark/Neon Theme**: Cyber-style design

## HFC Cipher Mapping

| Key | Output | | Key | Output |
|-----|--------|-|-----|--------|
| A | + | | N | ° |
| B | [] | | O | () |
| C | © | | P | ¶ |
| D | ÷ | | Q | ? |
| E | = | | R | ® |
| F | _ | | S | $ |
| G | > | | T | ™ |
| H | \| | | U | ' |
| I | ! | | V | ^ |
| J | # | | W | & |
| K | : | | X | ` |
| L | < | | Y | ~ |
| M | * | | Z | ℅ |

**Example**: HELLO → \| = < < ()

## Project Structure

```
hfc_keyboard/
└── android/
    └── app/
        └── src/main/
            ├── java/com/example/hfc_keyboard/
            │   ├── HFCKeyboardService.kt    # InputMethodService (keyboard)
            │   ├── HFCKeyboard.kt           # HFC cipher logic
            │   └── MainActivity.kt          # Settings app
            ├── res/
            │   ├── drawable/                # Icons, backgrounds
            │   ├── layout/                  # UI layouts
            │   ├── mipmap-*/                # App icons
            │   ├── values/                  # Colors, strings, themes
            │   └── xml/
            │       └── method.xml           # Input method configuration
            └── AndroidManifest.xml
```

## Build Instructions

### Prerequisites

1. **Android Studio** (Arctic Fox or newer)
2. **Android SDK** (API 34)
3. **Java JDK 11+**

### Build Steps

#### Method 1: Android Studio (Recommended)

1. Open Android Studio
2. Click **File → Open**
3. Select the `android` folder
4. Wait for Gradle sync
5. Click **Build → Build Bundle(s) / APK(s) → Build APK(s)**
6. APK will be at: `app/build/outputs/apk/release/app-release.apk`

#### Method 2: Command Line

```bash
cd android

# On Windows
gradlew.bat assembleRelease

# On Linux/Mac
./gradlew assembleRelease
```

APK location: `android/app/build/outputs/apk/release/app-release.apk`

### Install on Device

```bash
# Via ADB (USB debugging enabled)
adb install app/build/outputs/apk/release/app-release.apk

# Or transfer APK to device and tap to install
```

## Setup Guide

### 1. Enable Keyboard

1. Open **HFC Keyboard** app
2. Tap **ENABLE KEYBOARD**
3. Go to **Settings → System → Languages & input**
4. Tap **On-screen keyboards** → **Manage keyboards**
5. Enable **HFC Keyboard** (accept Android warning)

### 2. Select as Default

1. Open any app with text input (Notes, WhatsApp)
2. Tap the text field
3. Tap keyboard icon in notification bar OR long-press spacebar
4. Select **HFC Keyboard**

### 3. Configure Settings

Open HFC Keyboard app:
- **HFC Cipher Mode**: Enable/disable symbol replacement
- **Keypress Sound**: Toggle click sounds
- **Keypress Vibration**: Toggle haptic feedback
- **Dark Theme**: Toggle dark/light mode

## Technical Details

### InputMethodService

The keyboard is implemented as a native Android `InputMethodService`:

```kotlin
class HFCKeyboardService : InputMethodService() {
    override fun onCreateInputView(): View {
        return createKeyboardView()
    }
    
    private fun handleCharacterInput(char: Char) {
        val textToInsert = if (hfcModeEnabled && char.isLetter()) {
            HFCKeyboard.encodeCharacter(char)  // Real-time HFC conversion
        } else {
            char.toString()
        }
        currentInputConnection?.commitText(textToInsert, 1)
    }
}
```

### HFC Cipher

```kotlin
object HFCKeyboard {
    fun encodeCharacter(char: Char): String {
        return cipherMap[char.uppercaseChar()] ?: char.toString()
    }
}
```

### Backspace Handling

Multi-character symbols ([] and ()) are deleted as single units:

```kotlin
private fun handleBackspace() {
    val textBeforeCursor = inputConnection.getTextBeforeCursor(10, 0)
    val deleteCount = when {
        textBeforeCursor.endsWith("[]") -> 2
        textBeforeCursor.endsWith("()") -> 2
        else -> 1
    }
    repeat(deleteCount) {
        inputConnection.deleteSurroundingText(1, 0)
    }
}
```

## Permissions

```xml
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.INTERNET" />
```

## Privacy

- **No internet required** for core keyboard functionality
- **No data collection** - all processing is local
- **No telemetry** - completely offline

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Keyboard not showing | Enable in Settings → Languages & input → Manage keyboards |
| Can't select keyboard | Long-press spacebar in any text field |
| HFC not working | Check HFC Mode is enabled in settings |
| No sound/vibration | Enable in settings, check device volume |
| Build fails | Run `./gradlew clean` then rebuild |

## Screenshots

- Settings app with dark neon theme
- Real-time HFC typing in any app
- Full QWERTY keyboard with shift, numbers, symbols

## License

MIT License - See LICENSE file for details.

## Version

1.0.0 - Initial Release

## Credits

Built with ❤️ using Kotlin and Android InputMethodService
