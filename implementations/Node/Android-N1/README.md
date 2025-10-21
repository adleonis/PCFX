# PCF-X Atomizer Node (Android-N1)

A native Android implementation of the PCF-X **Atomizer Node**, which transforms `ExposureEvent` streams into `KnowledgeAtom` artifacts with NER, tone classification, and vector embeddings.

## What It Does

The Atomizer Node:

1. **Fetches ExposureEvents** from the PDV (Personal Data Vault) since the last watermark
2. **Atomizes** them into structured `KnowledgeAtom` artifacts containing:
   - **Named Entity Recognition (NER)**: Extracts brands, @handles, #hashtags, URLs
   - **Tone Classification**: Detects aspiration, authority, fear, scarcity, guilt, curiosity, social proof
   - **Vector Reference**: Generates local hash-based vector references
   - **Provenance**: Includes event ID, adapter ID, and node ID
3. **Publishes** atoms back to the PDV
4. **Supports scheduling**: Manual runs and daily automated runs at a configured time

## Requirements

- **Android 8.0+** (API 26, minSdk = 26)
- **PDV instance** running locally or remotely (e.g., `http://127.0.0.1:7777`)
- **Kotlin 2.2.0+**
- **Gradle 8.13.0+**

## Project Structure

```
implementations/Node/Android-N1/
├── build.gradle.kts              # Gradle build configuration
├── AndroidManifest.xml           # App manifest with permissions
├── proguard-rules.pro            # Obfuscation rules
├── src/main/
│   ├── kotlin/org/pcfx/node/androidn1/
│   │   ├── data/
│   │   │   ├── PdvService.kt     # Retrofit API service definition
│   │   │   └── PdvRepository.kt  # HTTP client with error handling
│   │   ├── domain/
│   │   │   └── Atomizer.kt       # Core pipeline (NER, tone, vectorizer)
│   │   ├── model/
│   │   │   ├── ExposureEvent.kt  # Input event model
│   │   │   └── KnowledgeAtom.kt  # Output atom model
│   │   ├── ui/
│   │   │   ├── MainActivity.kt     # Dashboard
│   │   │   ├── SettingsActivity.kt # PDV config
│   │   │   └── ScheduleActivity.kt # Schedule config
│   │   ├── util/
│   │   │   ├── PreferencesManager.kt  # DataStore preferences
│   │   │   └── SchemaValidator.kt    # JSON schema validation
│   │   └── work/
│   │       ├── AtomizeWorker.kt      # Execution service
│   │       ├── AlarmScheduler.kt     # AlarmManager scheduling
│   │       ├── DailyAlarmReceiver.kt # Daily alarm trigger
│   │       └── BootCompletedReceiver.kt # Reschedule on boot
│   ├── res/
│   │   ├── layout/                # Activity layouts
│   │   ├── values/strings.xml     # String resources
│   │   ├── values/styles.xml      # Style definitions
│   │   └── xml/network_security_config.xml
│   └── assets/                     # Optional JSON schemas
└── README.md
```

## Building

```bash
cd implementations/Node/Android-N1
./gradlew build
./gradlew assembleDebug
```

**Note**: APK will be available at: `build/outputs/apk/debug/Android-N1-debug.apk`

## Installation

```bash
adb install build/outputs/apk/debug/Android-N1-debug.apk
```

## Running

1. **Launch the app** → Opens Dashboard
2. **Configure PDV Settings** → Set PDV base URL (default: `http://127.0.0.1:7777`)
3. **Test Connectivity** → Verify PDV is reachable
4. **Run Now** → Manually trigger atomization (fetches events, atomizes, publishes atoms)
5. **Optional: Set Schedule** → Enable daily runs at a specific time (uses `AlarmManager`)

## Configuration

### PDV Base URL

Default: `http://127.0.0.1:7777`

Set via **PDV Settings** screen or by directly editing `PreferencesManager` defaults.

### Schedule

- **Enable/Disable** daily runs via **Schedule** screen
- **Time**: Select any HH:MM time; alarm reschedules after device boot (via `BootCompletedReceiver`)

### Watermark

- Stored in DataStore preferences
- Advanced only after successful atom publish
- Prevents double-processing of events

## PDV Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/events` | GET | Fetch `ExposureEvent`s since watermark |
| `/atoms` | POST | Publish `KnowledgeAtom` |
| `/atoms/batch` | POST | Publish multiple atoms (fallback) |

### Request Headers

```
X-PCFX-Cap: pdv.read.events, pdv.write.atoms
```

## Atomizer Pipeline Details

### Named Entity Recognition (NER)

Rule-based extraction:
- **Brands**: Capitalized words (e.g., "Brand X", "Apple")
- **Handles**: @-prefixed words (e.g., "@user")
- **Hashtags**: #-prefixed words (e.g., "#trending")
- **URLs**: HTTP(S) links
- **Deduplication**: By entity ID

### Tone Classification

Lexicon-based scoring (0.0 - 0.99):
- **Aspiration**: "amazing", "incredible", "best", "dream"
- **Authority**: "expert", "leader", "powerful", "control"
- **Fear**: "dangerous", "threat", "scary", "warning"
- **Scarcity**: "limited", "rare", "exclusive", "urgent"
- **Guilt**: "should", "must", "guilty", "shame"
- **Curiosity**: "wonder", "discover", "explore", "mystery"
- **Social Proof**: "popular", "trending", "viral", "favorite"

### Vectorization

**Current implementation**: Hashed bag-of-words
- Generates deterministic hash of text content
- Returns vector reference: `local://vectors/hash:{hex_hash}`

**Future enhancement**: TensorFlow Lite or ONNX Runtime Mobile for semantic embeddings

### Confidence Scoring

```kotlin
extraction = function(text_length, entity_count)
  - 0.92 if len > 50 and entities > 0
  - 0.85 if len > 20 and entities > 0
  - 0.75 if len > 10
  - 0.6 otherwise

verifiability = 0.3  (placeholder)
```

## Data Privacy

- **No raw content** logged or stored beyond processing
- **IDs and hashes only** in logs (event_id, atom_id, hash)
- **Respects retention_days** from ExposureEvent privacy field
- **Scoped capabilities**: `pdv.read.events`, `pdv.write.atoms`
- **Zero PII duplication**: Text is processed in-memory; no local persistence

## Error Handling

| Error | Behavior |
|-------|----------|
| **Rate Limit (429)** | Exponential backoff; does not advance watermark |
| **Server Error (5xx)** | Logs; does not advance watermark; retryable |
| **Validation Error (400)** | Logs; skips atom; continues batch |
| **Network Timeout** | Logs; stops batch; does not advance watermark |
| **Consent Denied** | Logs event ID; skips processing |

## Logging

All logging uses SLF4J + Logback:

```
adb logcat | grep "AtomizeWorker\|PdvRepository\|DefaultAtomizer"
```

Key tags:
- `AtomizeWorker`: Job execution
- `PdvRepository`: PDV HTTP calls
- `DefaultAtomizer`: Atomization pipeline
- `SchemaValidator`: Validation results

## Testing

### Unit Tests

```bash
./gradlew test
```

Tests cover:
- Atomizer transforms (NER, tone, confidence)
- Schema validation (required fields, types)
- Watermark logic (no double-process)

### Instrumented Tests

```bash
./gradlew connectedAndroidTest
```

Uses `MockWebServer` to simulate PDV:
- Mock `GET /events` responses
- Verify `POST /atoms` payloads

## Permissions

| Permission | Purpose |
|-----------|---------|
| `INTERNET` | PDV HTTP communication |
| `FOREGROUND_SERVICE` | Job notification |
| `FOREGROUND_SERVICE_DATA_SYNC` | Service foreground type |
| `RECEIVE_BOOT_COMPLETED` | Reschedule alarms on boot |
| `SCHEDULE_EXACT_ALARM` | Daily run at exact time |

## Manifest & Node ID

- **Node ID**: `pcfx.atomizer.android-n1/0.1.0`
- **Capabilities**: `pdv.read.events`, `pdv.write.atoms`
- **Component Manifest**: See `docs/ComponentManifest.json`

## Security Notes

1. **No hardcoded secrets**: PDV URL and tokens via DataStore
2. **Network security config**: Allows cleartext to `127.0.0.1` only (dev); HTTPS enforced elsewhere
3. **Signature validation**: Atoms signed with node key (placeholder; see future work)
4. **Obfuscation**: ProGuard rules enabled in release builds

## Future Enhancements

1. **Vector embeddings**: Replace hashed vectors with TensorFlow Lite small model
2. **Audio support**: Add speech-to-text (ASR) for audio blobs
3. **Relation extraction**: Implement `POST /relations` for template/duplicate detection
4. **Metrics**: Emit `POST /metrics` for aggregation (exposure velocity, burstiness)
5. **Signature validation**: Cryptographic signing of atoms (EDDSA)
6. **Resume on crash**: Save partial batch state; resume on restart

## Known Limitations

- **Rule-based NER only**: No ML model; future versions use TF-Lite
- **English text**: Tone/NER keywords are English only
- **No audio processing**: Audio blobs skipped; future work for ASR
- **No relation inference**: Single-atom output; relations computed by separate node
- **No signature**: Atoms unsigned (dev mode); production requires signing

## Troubleshooting

### "Failed to fetch events"
- Verify PDV base URL is correct
- Check network connectivity: `adb shell ping 127.0.0.1:7777`
- Ensure PDV is running and accessible

### "Rate limited"
- Wait 30+ seconds; scheduler will retry
- Check PDV quota/rate limit settings

### "Atom validation failed"
- Check logs for specific validation error
- Verify required fields in atom (text, tone, confidence, provenance)
- Contact PDV admin if schema mismatch

### "Schedule not triggering"
- Verify **SCHEDULE_EXACT_ALARM** permission granted
- Check if device is in Doze mode; `AlarmManager.setExactAndAllowWhileIdle` should wake it
- Restart app to reschedule

## Support & Contribution

For issues, questions, or contributions, see the main PCF-X repository:
- **Repo**: [https://github.com/pcfx/pcfx](https://github.com/pcfx/pcfx)
- **Spec**: `/spec/Node-Spec-v1.md`

---

**Version**: 0.1.0  
**Last Updated**: 2025-01-01  
**Maintainer**: PCF-X Foundation
