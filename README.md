# Systems-for-Notifying-and-Prompting-Users-to-Enter-Wellbeing-Data

This guide explains how to run this project built in Java using both Android Studio and the command line.

---

## ✅ 1. Using Android Studio

### Steps:
1. Open **Android Studio**.
2. Go to `File > Open` and select the project directory.
3. Let Gradle sync and finish building.
4. Start an emulator via `Tools > Device Manager` OR connect a real Android device.
5. Click the green **Run ▶** button or press **Shift + F10**.
6. Select your device if prompted. The app will launch automatically.

---

## 🗂️ Project Structure Overview

| File/Folder            | Description                                 |
|------------------------|---------------------------------------------|
| `build.gradle`         | Project and app build configuration         |
| `AndroidManifest.xml`  | App declarations and metadata               |
| `app/java/`            | Java source code                            |
| `res/`                 | Resources (layouts, images, strings, etc.)  |

---

## 📎 Appendix: Prerequisites

Before running the app, make sure you have the following tools installed and configured:

### 🔹 Java Development Kit (JDK)
- Version: JDK 8 or later
- Check with: `java -version`

### 🔹 Android Studio
- Install from: https://developer.android.com/studio
- Includes Android SDK and emulator setup tools.

### 🔹 Android SDK
- Ensure SDK tools are installed (via SDK Manager in Android Studio).
- Must include: `platform-tools`, `build-tools`, `emulator`

### 🔹 Gradle
- Included with Android Studio.
- No need to install separately unless you are using the command line only.
  - Here's the link to gradle official site https://gradle.org/

### 🔹 Android Debug Bridge (adb)
- Installed with Android SDK.
- Check with: `adb version`
- Required for installing and launching apps from the terminal.
  - https://developer.android.com/tools/releases/platform-tools

### 🔹 Physical Device or Emulator
- For emulator: use Android Studio's AVD Manager.
  - There's a lot out there; use any that you see fit. 
- For physical device: enable Developer Mode and USB Debugging.

---

```
