# HFC Keyboard - Build Guide (Hinglish)

## Quick Start (Jaldi Build)

### Android Studio Se (Sabse Aasaan)

1. **Android Studio** kholo
2. **File → Open** pe click karo
3. `android` folder select karo
4. Gradle sync hone do
5. **Build → Build APK(s)** pe click karo
6. APK ban jayega: `app/build/outputs/apk/release/app-release.apk`

### Command Line Se

```bash
cd android

# Windows pe
gradlew.bat assembleRelease

# Linux/Mac pe
./gradlew assembleRelease
```

APK milega: `android/app/build/outputs/apk/release/app-release.apk`

## Prerequisites (Zaroori Cheezein)

1. **Android Studio** (Arctic Fox ya newer)
   - Download: https://developer.android.com/studio

2. **Android SDK** (API 34)
   - Android Studio mein install ho jata hai

3. **Java JDK 11+**
   - Download: https://www.oracle.com/java/technologies/downloads/

## Step-by-Step Build

### Step 1: Project Open Karo

```
File → Open → android folder select karo
```

### Step 2: Gradle Sync

Android Studio automatically Gradle sync karega. Wait karo jab tak:
- "Gradle build finished" na dikhe
- Bottom mein "Sync Successful" na aaye

### Step 3: Build APK

```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

### Step 4: APK Install Karo

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

Ya APK ko phone mein transfer karke install karo.

## Common Errors aur Solutions

### ❌ "Gradle sync failed"

**Solution:**
```bash
# Clean build
gradlew.bat clean

# Phir se build
gradlew.bat assembleRelease
```

### ❌ "SDK not found"

**Solution:**
1. Android Studio kholo
2. **Tools → SDK Manager**
3. Android SDK Platform 34 install karo

### ❌ "JDK not found"

**Solution:**
1. File → Project Structure → SDK Location
2. JDK path set karo (e.g., `C:\Program Files\Java\jdk-17`)

### ❌ "Build failed - MinSdkVersion"

**Solution:**
- `build.gradle` mein `minSdk 24` hai
- Android 7.0+ device pe test karo

## Release APK vs Debug APK

| Type | Size | Signing | Speed |
|------|------|---------|-------|
| Debug | Bada | Debug key | Fast build |
| Release | Chota | Release key | Optimized |

**Release APK banane ke liye:**
```bash
gradlew.bat assembleRelease
```

## APK Verify Kaise Karein

```bash
# APK info dekho
adb shell dumpsys package com.example.hfc_keyboard

# Install check
adb shell pm list packages | grep hfc
```

## GitHub Pe Upload

```bash
# Git repo initialize karo
cd android
git init

# .gitignore already hai

# Files add karo
git add .

# Commit karo
git commit -m "Initial HFC Keyboard commit"

# GitHub pe push karo
git remote add origin https://github.com/yourusername/hfc-keyboard.git
git push -u origin main
```

## Releases Mein APK Daalna

1. GitHub repo pe jao
2. **Releases → Create new release**
3. Tag version: `v1.0.0`
4. APK upload karo: `app-release.apk`
5. Publish release

## Quick Commands Reference

```bash
# Clean build
gradlew.bat clean

# Debug APK
gradlew.bat assembleDebug

# Release APK
gradlew.bat assembleRelease

# Install on device
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall
adb uninstall com.example.hfc_keyboard

# Check logs
adb logcat | grep HFC
```

## Tips

✅ **Android Studio use karo** - Sabse easy hai
✅ **Debug APK se test karo** - Jaldi build hota hai
✅ **Release APK final mein banao** - Optimized hota hai
✅ **Device pe test karo** - Emulator slow ho sakta hai

## Help Chahiye?

Agar koi error aaye:
1. Error message dhyan se padho
2. `gradlew.bat clean` try karo
3. Android Studio invalidate caches: **File → Invalidate Caches**
4. Project close karke phir se open karo

---

**Happy Building! 🚀**
