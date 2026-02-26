# HFC Keyboard - Quick Setup (Hinglish)

## GitHub Pe Upload Kaise Karein

### Step 1: Git Repository Initialize Karo

```bash
cd "c:\Users\MRCOP\OneDrive\Desktop\vs code projects\hfc_keyboard\android"

# Git init karo
git init

# Sab files add karo
git add .

# Commit karo
git commit -m "Initial commit - HFC Keyboard"
```

### Step 2: GitHub Repository Banao

1. GitHub pe jao: https://github.com/new
2. Repository name: `hfc-keyboard`
3. Public/Private select karo
4. **Create repository** pe click karo

### Step 3: GitHub Se Connect Karo

```bash
# GitHub repo URL se connect karo
git remote add origin https://github.com/YOUR_USERNAME/hfc-keyboard.git

# Main branch pe rename karo
git branch -M main

# GitHub pe push karo
git push -u origin main
```

### Step 4: APK Download Ke Liye Release Banao

```bash
# Tag create karo
git tag v1.0.0

# Tag push karo
git push origin v1.0.0
```

Isse GitHub Actions automatically APK build karke release mein upload kar dega!

---

## Android Studio Mein Build (Local Testing)

1. **Android Studio** kholo
2. **File → Open** → `android` folder select karo
3. Gradle sync hone do
4. **Build → Build APK** pe click karo
5. APK ready: `app/build/outputs/apk/release/app-release.apk`

---

## GitHub Actions Se Auto Build

Jab bhi tum:
- `main` branch pe push karoge → APK build hoga (artifacts mein)
- Tag push karoge (e.g., `v1.0.0`) → Release banega with APK

### Check Build Status

1. GitHub repo pe jao
2. **Actions** tab pe click karo
3. Build status dekh sakte ho

### Download APK

1. **Actions** → Latest build select karo
2. **Artifacts** section mein `app-release.apk` milega
3. Download pe click karo

---

## Install & Test

```bash
# APK install karo
adb install app-release.apk

# App open karo
# Enable keyboard in settings
# Select HFC Keyboard as default
# Start typing!
```

---

## Important Files

| File | Description |
|------|-------------|
| `README.md` | Main documentation |
| `BUILD_GUIDE.md` | Detailed build guide |
| `.github/workflows/` | GitHub Actions CI/CD |
| `app/src/main/java/` | Source code |
| `app/build.gradle` | App dependencies |

---

## Troubleshooting

### ❌ Git push error

```bash
# Agar remote already exists
git remote remove origin
git remote add origin https://github.com/YOUR_USERNAME/hfc-keyboard.git
```

### ❌ Build failed

```bash
# Clean build
cd android
gradlew.bat clean
gradlew.bat assembleRelease
```

### ❌ GitHub Actions fail

- Check `.github/workflows/` files
- Verify JDK version (17)
- Check build logs in Actions tab

---

## Next Steps

1. ✅ GitHub pe code push karo
2. ✅ GitHub Actions se APK auto-build hoga
3. ✅ Release mein APK download hoga
4. ✅ Users install kar payenge

**Happy Coding! 🚀**
