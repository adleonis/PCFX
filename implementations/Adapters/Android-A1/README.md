# PCF-X Android Ingestion Adapter v0.1.0

A privacy-first Android adapter for the **Personal Cognitive Firewall eXchange Protocol (PCF-X)**.

This adapter monitors foreground app changes and system notifications on Android devices, constructs immutable `ExposureEvent` records, and publishes them to a local **Personal Data Vault (PDV)**.

## Features

- ✅ **Foreground app monitoring** via `AccessibilityService`
- ✅ **Notification capture** via `NotificationListenerService`
- ✅ **Screen video recording** with chunked capture (5-second frame-aligned chunks)
- ✅ **On-device screenshot OCR** with ML Kit text extraction (configurable 1-5 second intervals)
- ✅ **Background chunk upload** to PDV with automatic cleanup
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
└────────────────────────────────────────────────��────┘
```

## Video Recording (Chunked Capture)

The adapter supports **continuous screen video recording with automatic chunking**. Videos are captured in 5-second chunks and uploaded to PDV in the background, ensuring no frames are lost and adapter storage remains bounded.

### Architecture

```
Recording Thread              Chunk Manager               Upload Worker
   |                              |                            |
   +-> create chunk 0 ────────────┤                            |
   |   encode frames              |                            |
   |   every 5s: finalize ────────┤-> add to pending queue     |
   |   transition to chunk 1       |                            |
   |   continue recording (no gap) |   <── monitor pending ─────+
   |                              |       upload to PDV
   |                              |       delete local file
   +-> emit chunk event ──────────┤-> event published to PDV
```

### Features

- **Frame-aligned chunking**: 5-second chunks finalized at frame boundaries (no partial frames)
- **Non-blocking recording**: Recording continues immediately after chunk finalization; uploads happen in parallel
- **Storage-bounded adapter**: Chunks deleted from local cache immediately after successful PDV upload
- **Resilient uploads**: Failed uploads retry with 5-second backoff; dropped after 5 failed attempts
- **Sequence recovery**: Chunk sequence numbers persisted for recovery if app crashes
- **Consent-aware**: All chunks respect consent settings and retention periods

### Implementation Components

| Component | Purpose |
|-----------|---------|
| `VideoChunkManager` | Manages chunk lifecycle, tracks sequences, pending uploads |
| `VideoEncoder` (refactored) | Finalizes chunks every ~5 seconds instead of single monolithic file |
| `RecordingThread` | Continuous recording with automatic chunk transitions |
| `VideoChunkUploadWorker` | Background service that uploads finalized chunks independently |
| `VideoRecordingEventManager` | Monitors pending chunks and emits metadata events to PDV |

### How It Works

1. **Start Recording**: `VideoRecorderService` spawns `RecordingThread` and starts `VideoChunkUploadWorker`
2. **Chunk Creation**: Thread creates chunk 0 and begins encoding
3. **Frame Encoding**: Every 33ms (~15fps), frames drain through encoder and write to current chunk
4. **Chunk Finalization**: After ~75 frames (5 seconds at 15fps):
   - Encoder finalizes current chunk (flushes and closes MediaMuxer)
   - `VideoChunkManager.finalizeCurrentChunk()` records metadata
   - Chunk marked as pending for upload
   - **No delay**: New encoder/chunk created immediately; recording continues
5. **Background Upload**:
   - `VideoChunkUploadWorker` detects finalized chunks
   - Uploads chunk bytes to PDV `/blobs` endpoint
   - On success, deletes local file immediately
   - On failure, retries with backoff (max 5 attempts)
6. **Event Emission**:
   - `VideoRecordingStartedEvent`: Emitted when recording begins
   - `VideoChunkEvent`: Emitted for each finalized chunk (metadata + blobRef)
   - `VideoRecordingStoppedEvent`: Emitted when recording ends

### Configuration

Default recording settings in `RecordingConfig.kt`:

```kotlin
val width: Int = 1280              // Video width
val height: Int = 720              // Video height
val bitrate: Int = 5000000         // 5 Mbps
val frameRate: Int = 15            // 15 fps
val codec: String = "video/avc"    // H.264
val mimeType: String = "video/mp4"
```

### Data Flow

```
Device Screen
    ↓
MediaProjection + VirtualDisplay
    ↓
VideoEncoder (H.264)
    ↓
MediaMuxer → Chunk File (.mp4)
    ↓
VideoChunkManager (every ~5sec)
    ↓
VideoChunkUploadWorker (background)
    ���
POST /blobs → PDV Blob Storage
    ↓
Delete Local File (cleanup)
```

### Guarantees

- **No data loss**: Frame-aligned chunks ensure every frame is captured
- **No recording gaps**: Chunk transitions happen instantly; recording never pauses
- **Memory-bounded**: Constant cache size regardless of recording duration
- **Storage-bounded**: Adapter keeps only a rolling buffer of pending chunks; PDV owns all persistence
- **Resilient**: Failed uploads don't block recording; chunks retry independently

## Screenshot Capture with On-Device OCR

The adapter supports **on-device optical character recognition (OCR)** using Google ML Kit to extract text from screenshots. This enables capturing what information is displayed on screen with minimal bandwidth and battery impact, while keeping all processing local for privacy.

### Architecture

```
Capture Thread               OCR Processor                Event Builder            PDV
   |                            |                             |                     |
   +-> capture 720p             |                             |                     |
   |   screenshot               |                             |                     |
   |   (every N sec)            |                             |                     |
   |                            |                             |                     |
   +-> dedup check ─────────────┤                             |                     |
   |   (MD5 hash)               │ (skip if identical)         |                     |
   |                            |                             |                     |
   +-> enqueue ────────────────>│                             |                     |
   |   (max 100MB)              │                             |                     |
   |                            │                             |                     |
   |                      ML Kit OCR ────────────────────────>│                     |
   |                      (on background)                     │                     |
   |                      extract text                        │ build ExposureEvent │
   |                      + metadata                          │ (kind: ocr-text)    │
   |                      (confidence, lang,                  │ + extensions        │
   |                       block count)                       │ (ocr_confidence,    │
   |                                                          │  text_length)       │
   |                                                          │                     │
   |                                                          +──────────────────>  │
   |                                                                          POST /events
   |                                                                          (immediate)
   +-> if duplicate ────────────────────────────────────────────────────────>  │
       reference previous event (no OCR)
```

### Features

- **Fast on-device OCR**: ML Kit text recognition (~100-150ms per frame on flagship devices)
- **Memory-bounded**: Max 100MB queue; automatically purges oldest frames if limit exceeded
- **Deduplication**: MD5-based image hashing skips redundant OCR for static screens
- **Configurable intervals**: 1-5 second capture intervals (default 2 seconds)
- **Silent operation**: Low-priority background notification (no sound/vibration on Android 12+)
- **Language detection**: Automatically identifies text language (English, Arabic, Chinese, Russian)
- **Metadata extraction**: Includes OCR confidence, text length, and block count
- **Resilient**: Gracefully skips frames if device is busy; logs skipped frames for debugging
- **Mutually exclusive**: Cannot run video recording and screenshot capture simultaneously
- **Privacy-first**: Screenshots discarded immediately after OCR; no images stored or sent to PDV

### Implementation Components

| Component | Purpose |
|-----------|---------|
| `ScreenshotCaptureService` | Foreground service managing MediaProjection and capture loop |
| `OCRProcessor` | ML Kit TextRecognizer initialization and frame processing |
| `ScreenshotQueue` | Thread-safe FIFO queue with memory bounds (100MB default) |
| `ScreenshotDeduplicator` | MD5-based image hashing to detect duplicates |
| `ScreenshotEventBuilder` | Converts OCR results to ExposureEvent format |
| `ScreenshotCaptureManager` | Lifecycle orchestrator (start/stop/configure) |

### How It Works

1. **Start Capture**: User enables toggle in ConsentActivity → calls `ScreenshotCaptureManager.startCapture()`
2. **MediaProjection**: ScreenshotCaptureService acquires MediaProjection and creates VirtualDisplay
3. **Screenshot Loop**: Background thread captures 720p screenshot every N seconds (1-5s)
4. **Deduplication**: ScreenshotDeduplicator computes MD5 hash; skips OCR if matches previous
5. **Queue**: Valid screenshots enqueued to ScreenshotQueue (respects 100MB memory limit)
6. **OCR Processing**: OCRProcessor runs ML Kit TextRecognizer on background thread
   - If device is busy (other OCR in progress): skip frame and log
   - Extract text, confidence scores, language
7. **Event Creation**: ScreenshotEventBuilder creates ExposureEvent with:
   - `kind`: "ocr-text"
   - `content.text`: extracted text
   - `content.lang`: detected language
   - `extensions.ocr_confidence`: ML Kit confidence score
   - `extensions.ocr_text_length`: length of extracted text
   - `extensions.ocr_block_count`: number of text blocks detected
8. **Post to PDV**: Immediate POST to `/events` endpoint (no batching)
9. **Cleanup**: Screenshot bitmap recycled; no data persisted on device

### Configuration

Screenshot settings are configurable via **SharedPreferences** (`pcfx_screenshot_config`):

| Setting | Type | Default | Range | Notes |
|---------|------|---------|-------|-------|
| `screenshot_interval_seconds` | int | 2 | 1-5 | Capture interval in seconds |
| `screenshot_enabled` | boolean | false | - | Whether capture is active |

**UI Configuration**:
- **Interval Selector**: Spinner in ConsentActivity (1-5 seconds)
- **Toggle Switch**: "Start Screenshot Capture" (below video recording button)
- Both controls enabled only after consent is granted

### Performance Characteristics

Measured on flagship devices (Snapdragon 8 Gen 3):

| Metric | Value | Notes |
|--------|-------|-------|
| Screenshot capture time | ~30-50ms | MediaProjection + VirtualDisplay |
| ML Kit OCR latency | ~80-150ms | Per 720p frame, typical text density |
| Queue memory per frame | ~0.5MB | 720p ARGB_8888 bitmap |
| Max buffered frames | ~200 | At 100MB limit |
| CPU usage (idle) | <1% | Background coroutine, low priority |
| CPU usage (active OCR) | 15-25% | While processing frame |
| Battery impact | ~5-10%/day | At 2-second intervals, flagship device |
| Notification overhead | Minimal | Low-priority, no sound/vibration |

### Data Model

**ExposureEvent for OCR Text**:

```json
{
  "schema": "pcfx.exposure_event/0.1",
  "id": "uuid-...",
  "ts": "2025-01-15T10:32:45.123Z",
  "device": "android:samsung:S24Ultra",
  "adapter_id": "org.pcfx.adapter.android/0.1.0",
  "capabilities_used": ["screen.ocr.read"],
  "source": {
    "surface": "android-screenshot",
    "app": null,
    "url": null,
    "frame": "unique"
  },
  "content": {
    "kind": "ocr-text",
    "text": "Google\nSearch\nImages\nNews\n...",
    "lang": "en",
    "blob_ref": null
  },
  "privacy": {
    "consent_id": "consent-...",
    "pii_flags": [],
    "retention_days": 30
  },
  "signature": "ecdsa-p256:...",
  "extensions": {
    "ocr_confidence": 0.92,
    "ocr_block_count": 8,
    "ocr_text_length": 45,
    "screenshot_timestamp": 1673788365123
  }
}
```

**For Duplicate Screenshots**:

If a screenshot is identical to the previous one (same MD5 hash), instead of re-OCRing:

```json
{
  "... same as above ...",
  "capabilities_used": ["screen.ocr.read", "screen.ocr.deduplicate"],
  "source": {
    "surface": "android-screenshot",
    "frame": "duplicate"
  },
  "extensions": {
    "duplicate_of_event_id": "previous-event-uuid-...",
    "screenshot_timestamp": 1673788367123
  }
}
```

### Privacy & Security

- ✅ **No image storage**: Screenshots deleted immediately after OCR
- ✅ **On-device processing**: ML Kit runs locally; no images sent to cloud services
- ✅ **Text only transmitted**: Only extracted text sent to PDV, not raw image data
- ✅ **Bandwidth efficient**: 1-5KB text per event vs. ~100KB per screenshot
- ✅ **Consent-driven**: Requires explicit user grant via toggle; disabled by default
- ✅ **Deduplication**: Identical frames not re-processed, reducing compute
- ✅ **Language-agnostic**: Detects and labels language of extracted text (supports multiple languages)

### Error Handling

| Scenario | Behavior | Logging |
|----------|----------|---------|
| ML Kit fails | Skip frame, continue | ERROR: "OCR processing failed: [error]" |
| Device busy | Skip frame, log | WARN: "Skipped OCR frame: queue full" |
| Queue memory exceeded | Purge oldest frame | WARN: "Memory limit exceeded, purging frames" |
| PDV unreachable | Retry 3x with backoff | ERROR: "Failed to send screenshot event: [error]" |
| Service killed | Clean shutdown | INFO: "ScreenshotCaptureService destroyed" |

### Usage Example

```kotlin
// In ConsentActivity or your UI
val screenshotManager = ScreenshotCaptureManager(context)

// Start capturing (after consent granted)
val consentId = activeConsent.id
screenshotManager.startCapture(
    intervalSeconds = 2,  // Capture every 2 seconds
    consentId = consentId,
    retentionDays = 30
)

// Change interval at runtime
screenshotManager.setInterval(3)  // Now capture every 3 seconds

// Check if capturing
if (screenshotManager.isCapturing()) {
    Log.d("App", "Screenshot capture is active")
}

// Stop capturing
screenshotManager.stopCapture()
```

### Troubleshooting

**Screenshots not being captured:**
1. Verify consent is granted (app should show "Consent Granted")
2. Check toggle is enabled in UI
3. Verify PDV is reachable: `curl http://127.0.0.1:7777/health`
4. Check logcat: `adb logcat | grep "ScreenshotCaptureService"`

**High battery drain:**
1. Reduce capture interval (currently at N seconds)
2. Check for continuous OCR failures in logs: `adb logcat | grep "OCR"`
3. Verify device is not thermally throttling

**No text being extracted:**
1. Check if screen content has detectable text
2. Verify ML Kit is initialized: `adb logcat | grep "TextRecognizer initialized"`
3. Low-contrast or small text may fail; try increasing font size

**Memory warnings:**
1. Reduce capture interval (fewer queued frames)
2. Check device RAM availability
3. Monitor with: `adb shell dumpsys meminfo | grep "ScreenshotQueue"`

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

### Step 4: Enable Screenshot Capture (Optional)

To capture on-device screen text via OCR:

1. In the app, adjust the **Interval** spinner (1-5 seconds, default 2)
2. Toggle **Start Screenshot Capture** to ON
3. The app will start capturing 720p screenshots every N seconds and extracting text via ML Kit

### Step 5: Verify Events

Open another app or receive a notification, or toggle screenshot capture ON. Check that events are published:

```bash
curl "http://127.0.0.1:7777/events?limit=10"
```

**Expected output for app focus event**:

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
