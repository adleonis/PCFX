# PDV-1 Health Check Implementation

## Overview
The PDV-1 app now tracks health checks from connected adapters, nodes, and clients. Each connecting app is identified by a unique app ID and tracked with connection metrics.

## Database Schema

### HealthCheckEntity
- `appId` (String, Primary Key): Unique identifier for the connecting app
- `appType` (String): Type of app - "adapter", "node", or "client"
- `appName` (String): Display name of the app
- `appVersion` (String): Version of the app
- `platformInfo` (String): Platform information (e.g., "Android 14", "Node.js 20")
- `firstConnection` (Long): Timestamp of first connection to this PDV
- `lastConnected` (Long): Timestamp of most recent health check
- `connectionCount` (Int): Total number of health checks from this app

## Health Check Endpoint

### Endpoint: `GET /health`

The health check endpoint no longer uses query parameters. Instead, it reads app information from HTTP headers:

**Required Headers:**
- `X-App-ID`: Unique identifier for the app (UUID recommended)
- `X-App-Type`: Type of app ("adapter", "node", or "client")
- `X-App-Name`: Display name (e.g., "Android-A1")
- `X-App-Version`: Version string (e.g., "1.0.0")
- `X-Platform-Info`: Platform information (e.g., "Android 34", "Node.js 20.0")

**Response:**
```json
{
  "status": "healthy"
}
```

### Example: Adapter Health Check
```
GET /health HTTP/1.1
Host: 127.0.0.1:7777
X-App-ID: 550e8400-e29b-41d4-a716-446655440000
X-App-Type: adapter
X-App-Name: Android-A1
X-App-Version: 1.0.0
X-Platform-Info: Android 34
```

### Example: Node Health Check
```
GET /health HTTP/1.1
Host: 127.0.0.1:7777
X-App-ID: 6ba7b810-9dad-11d1-80b4-00c04fd430c8
X-App-Type: node
X-App-Name: Node-N1
X-App-Version: 2.0.0
X-Platform-Info: Node.js 20.0
```

## Unique App ID Generation

Each app should generate a unique app ID during the build/compilation phase. This ID should be:
- Persistent across app reinstallations
- Unique to each app build
- Stored as a build-time constant

### For Android Apps (Adapters):

1. Generate a UUID during build configuration
2. Store in a build config constant (similar to `AppBuildConfig.kt`)
3. Include in the APK so it's accessible at runtime

```kotlin
// In build.gradle.kts
android {
    defaultConfig {
        // Generate unique ID for this build
        val appId = java.util.UUID.randomUUID().toString()
        buildConfigField("String", "APP_ID", "\"$appId\"")
    }
}
```

Then access it in code:
```kotlin
val appId = BuildConfig.APP_ID
```

### For Node Apps (Nodes):

1. Generate a UUID in the build/compilation step
2. Store in an environment variable or config file
3. Access at runtime when making health check requests

```javascript
// In build or startup script
const appId = require('crypto').randomUUID();
process.env.APP_ID = appId;

// In health check code
const appId = process.env.APP_ID;
```

## Health Check Persistence Behavior

1. **First Connection**: When an app with a new app ID sends a health check:
   - A new `HealthCheckEntity` is created
   - `firstConnection` is set to current timestamp
   - `lastConnected` is set to current timestamp
   - `connectionCount` is set to 1

2. **Subsequent Connections**: When the same app (same app ID) sends another health check:
   - The existing `HealthCheckEntity` is updated
   - `lastConnected` is updated to current timestamp
   - `connectionCount` is incremented by 1
   - `firstConnection` remains unchanged

## Stats Endpoint

### Endpoint: `GET /stats`

**Response:**
```json
{
  "events": 42,
  "atoms": 100,
  "metrics": 256,
  "blobs": 10,
  "adapters": {
    "total": 2,
    "active_24h": 2
  },
  "nodes": {
    "total": 1,
    "active_24h": 1
  },
  "clients": {
    "total": 0,
    "active_24h": 0
  }
}
```

**Statistics Calculation:**
- `total`: Count of all unique apps that have ever connected (using `firstConnection` timestamp)
- `active_24h`: Count of unique apps that have connected in the last 24 hours (using `lastConnected` timestamp)

## Implementation Notes

- The `ConnectionTracker` class now uses `HealthCheckRepository` as the source of truth
- All health check data is persisted in the SQLite database
- Health check records are only updated when the same app ID sends a new request
- The system is designed to minimize database records (one per unique app)
- Future pruning of old health checks can be implemented based on retention policies

## Next Steps

1. Update Android-A1 adapter to:
   - Generate unique app ID at build time
   - Send health checks to PDV with proper headers
   - Display app ID in settings/info page

2. Update Node-N1 to:
   - Generate unique app ID at build time
   - Send health checks to PDV with proper headers
   - Display app ID in settings/info page

3. Update PDV-1 UI:
   - Display app ID on settings/info page
   - Remove unused 7d and 30d views from dashboard layout
   - Update dashboard layout to only show Total and Last 24h
