# PDV-1: On-Device Personal Data Vault

PDV-1 is a standalone Android application that implements a local, embedded HTTP server providing the Personal Data Vault (PDV) functionality for the PCFX protocol. It manages exposure events from adapters, knowledge atoms and metrics from nodes, and enforces consent and capability-based access control.

## Architecture

PDV-1 follows a layered architecture:

### Data Layer
- **Room Database**: SQLite-based persistent storage
  - Events (from adapters)
  - Atoms (from nodes)
  - Metrics (from nodes)
  - Blobs (media assets)
  - Consents (capability tokens)

### Domain Layer
- **Repositories**: Implement business logic for each data type
  - EventRepository
  - AtomRepository
  - MetricRepository
  - BlobRepository
  - ConsentRepository

### Server Layer
- **Ktor HTTP Server**: Embedded lightweight HTTP server
  - REST API endpoints
  - JSON serialization/deserialization
  - Content negotiation

### Service Layer
- **PdvServerService**: Android foreground service
  - Runs HTTP server in background
  - Lifecycle management
  - Notification integration

### UI Layer
- **MainActivity**: Simple dashboard for server control
  - Start/Stop buttons
  - Port configuration
  - Auto-start toggle

## API Endpoints (Phase 1 & 2)

### Events
- `POST /events` - Adapters publish ExposureEvents
- `GET /events?since=<iso8601>&limit=<int>` - Nodes fetch events

### Atoms
- `POST /atoms` - Nodes publish KnowledgeAtoms
- `GET /atoms?since=<iso8601>&limit=<int>` - Clients fetch atoms

### Metrics
- `POST /metrics` - Nodes publish Metrics
- `GET /metrics?since=<iso8601>&limit=<int>` - Clients fetch metrics

### Blobs
- `POST /blobs` - Store media (audio/video/image)
- `GET /blobs/{hash}` - Retrieve blob by SHA256 hash

### Consents
- `POST /consents` - Register capability tokens (Phase 2)
- `GET /consents/{id}` - Retrieve consent record (Phase 2)

### Statistics
- `GET /stats` - Get current data counts (events, atoms, metrics, blobs)

## Phase 1: Core PDV Service

Implemented features:
- ✅ Ktor HTTP server on port 7777 (configurable)
- ✅ Room database with all required entities
- ✅ REST API endpoints for events, atoms, metrics, blobs
- ✅ Android foreground service for background operation
- ✅ Simple UI for start/stop and configuration
- ✅ Boot completion receiver for auto-start
- ✅ JSON serialization/deserialization (Gson)
- ✅ SHA256 hashing for blob content

## Phase 2: Capability & Consent Enforcement

Implemented features:
- ✅ ConsentEntity and ConsentDao for storing consent records
- ✅ ConsentRepository with capability verification logic
- ✅ Support for adapter and node consent tokens
- ✅ Expiration checking for consents
- ✅ `verifyCapability()` method to validate component capabilities

**Note**: API endpoints do not yet enforce capability checks. Integration of capability enforcement into HTTP endpoints is a future enhancement.

## Building

```bash
cd implementations/PDV/PDV-1
./gradlew clean build
./gradlew assembleDebug  # Build debug APK
./gradlew installDebug   # Install on connected device/emulator
```

## Running

1. Start the app from your Android device/emulator
2. Tap "Start Server" to begin the PDV server
3. Server listens on `http://127.0.0.1:7777` (or configured port)
4. Configure port using SeekBar (7000-8000 range)
5. Enable "Auto-start PDV Server on boot" to auto-start on device boot

## Project Structure

```
implementations/PDV/PDV-1/
├── src/main/
│   ├── kotlin/org/pcfx/pdv/androidpdv1/
│   │   ├── data/
│   │   │   ├── entity/        # Room entities
│   │   │   ├── dao/           # Data Access Objects
│   │   │   └── PdvDatabase.kt # Room database definition
│   │   ├── domain/
│   │   │   └── *Repository.kt # Business logic layer
│   │   ├── server/
│   │   │   └── PdvServer.kt   # Ktor HTTP server
│   │   ├── service/
│   │   │   └── PdvServerService.kt # Android foreground service
│   │   ├── receiver/
│   │   │   └── BootCompletedReceiver.kt
│   │   └── ui/
│   │       └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   ├── values/
│   │   └── xml/
│   └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Dependencies

- **Ktor**: Embedded HTTP server
- **Room**: Local database
- **Kotlin Coroutines**: Async operations
- **Gson**: JSON serialization
- **DataStore**: Configuration storage
- **BouncyCastle**: Cryptography

## Future Enhancements

- HTTP endpoint capability enforcement (Phase 2)
- Signature verification for events/atoms/consents
- Multi-device sync capability
- Cloud migration (Phase 3)
- Encryption at rest for sensitive data
- Advanced query filters
- Event streaming (WebSocket/Server-Sent Events)

## Security Considerations

- Network security config restricts cleartext to localhost only
- Foreground service prevents background termination
- Database operates with Android app context security
- Blob storage uses SHA256 hashing
- Consent expiration enforcement
