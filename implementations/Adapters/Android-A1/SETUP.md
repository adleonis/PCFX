# PCF-X Android Adapter - Setup Guide

## Prerequisites

Before you begin, ensure you have:

- **Android Studio** (latest version with Android API 34 SDK)
- **Java Development Kit (JDK) 11** or higher
- **Android emulator** or physical Android device (API 26+)
- **PCF-X PDV** running locally at `http://127.0.0.1:7777`
- **Git** for version control

## Installation Steps

### 1. Clone the Repository

```bash
git clone https://github.com/pcfx-foundation/pcfx-android-adapter.git
cd implementations/Adapters/Android-A1
```

### 2. Open in Android Studio

1. Open **Android Studio**
2. Click **File → Open**
3. Navigate to `implementations/Adapters/Android-A1`
4. Click **OK**

Android Studio will automatically sync Gradle dependencies (this may take a few minutes).

### 3. Configure PDV Endpoint

By default, the adapter communicates with PDV at `http://127.0.0.1:7777`.

To change this:

1. Open `src/main/kotlin/org/pcfx/adapter/android/network/PDVClient.kt`
2. Modify the `PREFS_DEFAULT_URL` constant:
   ```kotlin
   private const val PREFS_DEFAULT_URL = "http://your-pdv-server:7777"
   ```

Or at runtime via the ConsentActivity UI.

### 4. Build the Project

```bash
# Via command line
./gradlew build

# Or via Android Studio:
# Build → Build Bundle(s) / APK(s) → Build APK(s)
```

Expected output:
```
BUILD SUCCESSFUL in Xs
```

### 5. Deploy to Device/Emulator

#### Option A: Android Emulator

```bash
# Start emulator (if not already running)
$ANDROID_HOME/emulator/emulator -avd Pixel_7_API_34 -no-audio

# Install the app
./gradlew installDebug

# Expected: Install of app-debug.apk successful
```

#### Option B: Physical Device

```bash
# Enable Developer Mode on your device:
# Settings → About Phone → tap "Build Number" 7 times
# Settings → Developer Options → enable "USB Debugging"

# Connect device via USB

# Install the app
./gradlew installDebug
```

### 6. Grant Required Permissions

**Accessibility Service:**
1. On device, go to **Settings → Accessibility**
2. Find and tap **PCF-X Adapter**
3. Toggle **ON**
4. Grant permission when prompted

**Notification Listener Service:**
1. Go to **Settings → Apps & notifications → Notifications → Notification Access** (varies by device)
2. Find and enable **PCF-X Adapter**

## Verifying Installation

### Step 1: Start PDV

Ensure PDV is running:

```bash
# Health check
curl http://127.0.0.1:7777/health

# Expected response
{"status": "ok", "pdv_version": "0.1.0"}
```

### Step 2: Open the App

1. On device, find **PCF-X Adapter** app
2. Tap to open

You should see:
- Title: "PCF-X Adapter: Privacy & Consent Request"
- List of capabilities being requested
- Buttons: "Accept Consent", "Decline", "View Consent Status", "Regenerate Key Pair"

### Step 3: Grant Consent

1. Click **Accept Consent**
2. A toast message should appear: "Consent granted. Event publishing started."

### Step 4: Trigger Events

To generate exposure events:

1. **Open another app** (e.g., Chrome, Gmail)
   - An `ExposureEvent` with `surface="app"` is created
2. **Receive a notification** (e.g., send yourself a message via WhatsApp)
   - An `ExposureEvent` with `surface="system"` is created

### Step 5: Verify Events in PDV

Check that events were posted:

```bash
curl "http://127.0.0.1:7777/events?limit=5"

# Expected: JSON array with ExposureEvent objects
```

Sample event:

```json
{
  "schema": "pcfx.exposure_event/0.1",
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "ts": "2025-10-20T16:45:23.456Z",
  "device": "android:google:Pixel7",
  "adapter_id": "org.pcfx.adapter.android/0.1.0",
  "capabilities_used": ["screen.focus.read"],
  "source": {
    "surface": "app",
    "app": "com.google.android.gms",
    "frame": "main"
  },
  "content": {
    "kind": "text",
    "text": "Google Play Services",
    "lang": "en"
  },
  "privacy": {
    "consent_id": "abc12345-def6-7890-abcd-ef1234567890",
    "pii_flags": [],
    "retention_days": 30
  },
  "signature": "ecdsa-p256:..."
}
```

## Development Workflow

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests EventBuilderTest

# Run with code coverage
./gradlew test jacocoTestReport
```

### Building for Release

```bash
# Build release APK with ProGuard/R8
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

### Debugging

#### Via Logcat

```bash
# Filter logs for the adapter
adb logcat | grep "org.pcfx"

# Or use Android Studio:
# View → Tool Windows → Logcat
```

#### Breakpoint Debugging

1. In Android Studio, set breakpoints in code
2. Click **Run → Debug 'app'**
3. App will pause at breakpoints

#### Database Inspection

```bash
# View RoomDB via Device File Explorer
adb shell
run-as org.pcfx.adapter.android
cat /data/data/org.pcfx.adapter.android/databases/pcfx_adapter.db

# Or use Android Studio Database Inspector:
# View → Tool Windows → App Inspection → Database Inspector
```

## Troubleshooting

### Build Issues

**Problem:** `Error: ... Gradle sync failed`

**Solution:**
1. Click **File → Invalidate Caches** and restart
2. Delete `.gradle` folder: `rm -rf ~/.gradle`
3. Resync: **File → Sync Now**

### Permission Issues

**Problem:** Accessibility or Notification services not enabling

**Solution:**
1. Uninstall app: `./gradlew uninstallDebug`
2. Clear cache: `adb shell pm clear org.pcfx.adapter.android`
3. Reinstall: `./gradlew installDebug`
4. Manually grant permissions in Settings

### PDV Connection Issues

**Problem:** Events not being posted (PDV unreachable)

**Solution:**
1. Check PDV is running: `curl http://127.0.0.1:7777/health`
2. Verify network connectivity on device/emulator
3. Check for firewall blocking port 7777
4. Review logs: `adb logcat | grep "EventPublisher"`

### Consent Not Persisting

**Problem:** Consent status resets after app restart

**Solution:**
1. Verify SharedPreferences is working: check logs for `ConsentManager`
2. Check device has sufficient storage
3. Clear and rebuild: `./gradlew clean build`

## Environment Variables

For CI/CD integration:

```bash
# Set PDV_URL for remote PDV (default: http://127.0.0.1:7777)
export PDV_URL="http://pdv-server:7777"

# Build and test
./gradlew build
```

## Next Steps

- Review the main **README.md** for feature details
- Check **adapter_manifest.json** for capability declarations
- Explore **spec/** directory for PCF-X protocol details
- Run unit tests: `./gradlew test`

## Support

- **Issues:** Open a GitHub issue with logs
- **Discussions:** Use GitHub Discussions for questions
- **Security:** Report vulnerabilities privately to [security@pcfx.org](mailto:security@pcfx.org)

---

**Last Updated:** 2025-10-20 | **Status:** Alpha
