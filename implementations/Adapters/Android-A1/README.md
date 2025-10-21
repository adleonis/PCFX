# PCF-X Android Ingestion Adapter v0.1.0

A privacy-first Android adapter for the **Personal Cognitive Firewall eXchange Protocol (PCF-X)**.

This adapter monitors foreground app changes and system notifications on Android devices, constructs immutable `ExposureEvent` records, and publishes them to a local **Personal Data Vault (PDV)**.

## Features

- ✅ **Foreground app monitoring** via `AccessibilityService`
- ✅ **Notification capture** via `NotificationListenerService`
- ✅ **Offline queue** with RoomDB and auto-flush when PDV comes online
- ✅ **Ed25519 signing** with Android KeyStore for key storage
- ✅ **Blob upload** support for media artifacts
- ✅ **Consent-driven** with explicit user grant flow
- ✅ **JSON schema validation** for all events
- ✅ **Exponential backoff retry** logic (500ms base, 60s max)
- ✅ **Persistent notification** showing adapter status
- ✅ **Unit tests** with event builder, validator, and key manager

## Architecture

```
┌─────────────────────────────────────────────────────┐
│           Android Device                            │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │ AccessibilityService + NotificationListener  │  │
│  │ → Capture app focus & notifications          │  │
│  └──────────────┬───────────────────────────────┘  │
│                 │                                   │
│  ┌──────────────▼───────────────────────────────┐  │
│  │ EventBuilder                                 │  │
│  │ → Create ExposureEvent + sign with Ed25519   │  │
│  └──────────────┬───────────────────────────────┘  │
│                 │                                   │
│  ┌──────────────▼───────────────────────────────┐  │
│  │ RoomDB (EventQueue)                          │  │
│  │ → Store unposted events locally              │  │
│  └──────────────┬───────────────────────────────┘  │
│                 │                                   │
│  ┌──────────────▼───────────────────────────────┐  │
│  │ EventPublisherService                        │  │
│  │ → Batch & post events to PDV /events         │  │
│  └──────────────┬───────────────────────────────┘  │
│                 │                                   │
│                 ▼                                   │
│  http://127.0.0.1:7777/events (PDV)               │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## Prerequisites

- Android API 26+ (target SDK 34)
- Android Studio with Kotlin support
- PDV running locally at `http://127.0.0.1:7777`

## Build Instructions

### 1. Clone and Setup

```bash
cd implementations/Adapters/Android-A1
```

### 2. Configure PDV URL (Optional)

The adapter defaults to `http://127.0.0.1:7777`. To change it at runtime, edit the ConsentActivity or use:

```kotlin
val pdvClient = PDVClient(context)
pdvClient.setPDVUrl("http://your-pdv-server:7777")
```

### 3. Build the App

```bash
# Using Gradle
./gradlew build

# Or in Android Studio: Build → Build Bundle(s) / APK(s)
```

### 4. Install on Device/Emulator

```bash
./gradlew installDebug

# Or manually
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Running the Adapter

### Step 1: Start PDV

Ensure the PDV is running and accessible at `http://127.0.0.1:7777`:

```bash
# Example PDV health check
curl http://127.0.0.1:7777/health
# Expected: {"status": "ok", "pdv_version": "0.1.0"}
```

### Step 2: Grant Permissions

1. Open the **PCF-X Adapter** app
2. Navigate to **Settings → Accessibility** and enable **PCF-X Adapter**
3. Navigate to **Settings → Notification Manager** and allow notifications from **PCF-X Adapter**

### Step 3: Grant Consent

1. Open the app
2. Review the consent request (shows `screen.focus.read`, `notifications.read`)
3. Click **Accept Consent**
4. The adapter is now active and will start capturing events

### Step 4: Verify Events

Open another app or receive a notification. Check that events are published:

```bash
curl "http://127.0.0.1:7777/events?limit=10"
```

Expected output (first event):

```json
{
  "schema": "pcfx.exposure_event/0.1",
  "id": "35b0a8e0-e0c3-...",
  "ts": "2025-10-20T14:32:11.123Z",
  "device": "android:google:Pixel8",
  "adapter_id": "org.pcfx.adapter.android/0.1.0",
  "capabilities_used": ["screen.focus.read"],
  "source": {
    "surface": "app",
    "app": "com.instagram.android",
    "frame": "main"
  },
  "content": {
    "kind": "text",
    "text": "Instagram",
    "lang": "en"
  },
  "privacy": {
    "consent_id": "consent-7f3a-...",
    "pii_flags": [],
    "retention_days": 30
  },
  "signature": "ecdsa-p256:..."
}
```

## Configuration

### Consent Management

Consent is stored in **SharedPreferences** under `pcfx_consent`. To revoke consent:

1. Open the app
2. Click **Revoke Consent**
3. The adapter will stop capturing immediately

### Key Pair Management

The Ed25519 key pair is stored in **Android KeyStore**. To regenerate:

1. Open the app
2. Click **Regenerate Key Pair**
3. A new keypair is created and stored securely

### PDV URL Configuration

The PDV URL is stored in **SharedPreferences** under `pcfx_pdv_config`. Default: `http://127.0.0.1:7777`

To change programmatically:

```kotlin
val pdvClient = PDVClient(context)
pdvClient.setPDVUrl("http://custom-pdv-server:7777")
```

## Event Schema

All events conform to **`pcfx.exposure_event/0.1`** schema. Key fields:

| Field | Type | Required | Example |
|-------|------|----------|---------|
| `schema` | string | ✅ | `"pcfx.exposure_event/0.1"` |
| `id` | UUID | ✅ | `"35b0a8e0-e0c3-..."` |
| `ts` | ISO-8601 | ✅ | `"2025-10-20T14:32:11.123Z"` |
| `device` | string | ✅ | `"android:google:Pixel8"` |
| `adapter_id` | string | ✅ | `"org.pcfx.adapter.android/0.1.0"` |
| `capabilities_used` | array | ✅ | `["screen.focus.read"]` |
| `source.surface` | enum | ✅ | `"app"`, `"system"` |
| `source.app` | string | ✅ | `"com.instagram.android"` |
| `content.kind` | enum | ✅ | `"text"` |
| `content.text` | string | ✅ | `"Instagram"` |
| `privacy.consent_id` | UUID | ✅ | `"consent-7f3a-..."` |
| `privacy.retention_days` | int | ✅ | `30` |
| `signature` | string | ✅ | `"ecdsa-p256:..."` |

## Testing

### Run Unit Tests

```bash
./gradlew test
```

Tests include:
- **EventBuilderTest**: Validates event construction and signing
- **EventValidatorTest**: Checks JSON schema compliance
- **KeyManagerTest**: Verifies key generation and storage

### Test Coverage

```
- EventBuilder: app focus, notifications, media blobs
- EventValidator: schema validation, required fields, enum values
- KeyManager: key generation, storage, regeneration
```

## Troubleshooting

### Events Not Appearing

1. **Check PDV is running:** `curl http://127.0.0.1:7777/health`
2. **Verify consent is granted:** Open app → Check if "Consent Granted" button is shown
3. **Check permissions:** Settings → Accessibility & Notification Manager should have adapter enabled
4. **Check logs:** `adb logcat | grep "EventPublisher"`

### Accessibility Service Not Starting

1. Go to **Settings → Accessibility**
2. Find **PCF-X Adapter** and toggle ON
3. Grant permission when prompted

### Notification Listener Not Working

1. Go to **Settings → Notification Manager**
2. Find **PCF-X Adapter** and grant permission
3. Restart the app

### Key Management Issues

- If key regeneration fails, try clearing app cache: `adb shell pm clear org.pcfx.adapter.android`
- Retrieve the current public key (for audit): `adb logcat | grep "public_key_base64"`

## Offline Queue Behavior

The adapter uses **RoomDB** to queue events when PDV is unavailable:

- Max queue size: unlimited (disk-limited)
- Batch size: 32 events per POST
- Retry strategy: exponential backoff (500ms → 60s)
- Max retries per event: 5 attempts
- Auto-cleanup: posted events deleted after 30 days

## Privacy & Minimal PII

The adapter follows privacy-by-design principles:

- ✅ **No emails, phone numbers, or contact names** captured
- ✅ **No passwords or secrets** transmitted
- ✅ **App package names only** (no deep app state)
- ✅ **Window titles truncated** to 500 chars
- ✅ **Notification text snippet** only (not full payload)
- ✅ **No network traffic** outside PDV (except blob uploads)
- ✅ **All signatures verified** by PDV before storage

## Manifest & SBOM

- **Adapter Manifest:** `adapter_manifest.json` (describes capabilities)
- **SBOM:** `SBOM.md` (list of dependencies)

## Next Steps

- **Microphone capture** (future v0.2): requires `microphone.capture` capability
- **Video/screenshot blobs** (future): requires user consent
- **Federated analytics** (future): anonymized metrics aggregation

## License

Licensed under **Apache License 2.0** and **CC-BY-SA 4.0**.

See root `/LICENSE_APACHE2` and `/LICENSE_CC-BY-SA`.

## Support & Issues

- **Documentation:** [PCF-X Specification](../../spec/)
- **Issues:** Report via GitHub Issues
- **Pull Requests:** Welcome! Follow contribution guidelines in `/CONTRIBUTING.md`

---

**Version:** 0.1.0 | **Last Updated:** 2025-10-20 | **Status:** Alpha
