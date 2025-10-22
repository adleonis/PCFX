# Developer Guide

## How to Build and Integrate Components

This guide covers building and integrating Adapters, Nodes, and Clients with the PDV (Personal Data Vault) system.

### Table of Contents

1. [Health Check Registration](#health-check-registration)
2. [Adapter Development](#adapter-development)
3. [Node Development](#node-development)
4. [Client Development](#client-development)
5. [Best Practices](#best-practices)
6. [Testing & Debugging](#testing--debugging)

---

## Health Check Registration

Every component (Adapter, Node, Client) MUST implement health check registration to allow the PDV to track its status and maintain accurate statistics.

### What is a Health Check?

A health check is a lightweight HTTP GET request to the PDV's `/health` endpoint that serves two purposes:

1. **Component Discovery:** The PDV learns about all installed components and their metadata
2. **Activity Tracking:** The PDV tracks when each component last connected and counts total connections

### Health Check Frequency

* **Adapters:** Every 5-30 minutes, or whenever they successfully connect to perform operations
* **Nodes:** Every 5-30 minutes, or whenever they successfully connect to fetch/process data
* **Clients:** Every 10-60 minutes, or whenever they refresh UI data (optional but recommended)

### Required Headers

All health check requests MUST include these HTTP headers:

```
X-App-ID: <unique-uuid>               # Unique app identifier (generated at build time)
X-App-Type: adapter|node|client        # Component type
X-App-Name: <display-name>             # e.g., "Android-A1", "Node-N1", "Dashboard"
X-App-Version: <version-string>        # Semantic version, e.g., "1.0.0"
X-Platform-Info: <platform-details>    # e.g., "Android 34", "Node.js 20.0", "Web/Chrome 120"
```

### Unique App ID Generation

The `X-App-ID` header must be a unique identifier that persists across app builds and reinstalls:

#### For Android Components (Adapters, Nodes):

**Option 1: Build-time Generation (Recommended)**

Generate a UUID during the build process and embed it as a constant:

```kotlin
// build.gradle.kts
android {
    defaultConfig {
        val appId = java.util.UUID.randomUUID().toString()
        buildConfigField("String", "APP_ID", "\"$appId\"")
    }
}

// At runtime, in your component code:
val appId = BuildConfig.APP_ID
```

**Option 2: First-Launch Generation**

Generate and persist the ID on first app launch:

```kotlin
val appId = SharedPreferences.getString("app_id") ?: run {
    val id = UUID.randomUUID().toString()
    SharedPreferences.putString("app_id", id)
    id
}
```

#### For Node Components (JavaScript/TypeScript):

**Option 1: Build-time Generation (Recommended)**

```typescript
// In your build script or environment:
export const APP_ID = require('crypto').randomUUID();

// In your component code:
import { APP_ID } from './build-constants';
```

**Option 2: Persistent Storage**

```typescript
const crypto = require('crypto');
const fs = require('fs');
const configFile = './app-id.json';

const APP_ID = (() => {
    if (fs.existsSync(configFile)) {
        return JSON.parse(fs.readFileSync(configFile)).app_id;
    } else {
        const id = crypto.randomUUID();
        fs.writeFileSync(configFile, JSON.stringify({ app_id: id }));
        return id;
    }
})();
```

#### For Web Components (JavaScript/React):

```javascript
// Store in localStorage for persistence across sessions:
const APP_ID = localStorage.getItem('app_id') || 
               (() => {
                   const id = crypto.randomUUID();
                   localStorage.setItem('app_id', id);
                   return id;
               })();
```

### Implementation Examples

#### Kotlin (Android Adapter)

```kotlin
import okhttp3.OkHttpClient
import okhttp3.Request
import android.os.Build
import android.util.Log

class HealthCheckClient(private val pdvUrl: String = "http://127.0.0.1:7777") {
    private val httpClient = OkHttpClient()
    
    fun sendHealthCheck(
        appId: String,
        appName: String,
        appVersion: String
    ) {
        try {
            val platformInfo = "Android ${Build.VERSION.SDK_INT}"
            val request = Request.Builder()
                .url("$pdvUrl/health")
                .header("X-App-ID", appId)
                .header("X-App-Type", "adapter")
                .header("X-App-Name", appName)
                .header("X-App-Version", appVersion)
                .header("X-Platform-Info", platformInfo)
                .get()
                .build()
            
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.d("HealthCheck", "Sent successfully")
                } else {
                    Log.w("HealthCheck", "Failed: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e("HealthCheck", "Error sending health check", e)
        }
    }
}
```

#### JavaScript (Node Component)

```javascript
const fetch = require('node-fetch');

class HealthCheckClient {
    constructor(pdvUrl = 'http://127.0.0.1:7777') {
        this.pdvUrl = pdvUrl;
    }
    
    async sendHealthCheck(appId, appName, appVersion) {
        try {
            const platformInfo = `Node.js ${process.version}`;
            
            const response = await fetch(`${this.pdvUrl}/health`, {
                method: 'GET',
                headers: {
                    'X-App-ID': appId,
                    'X-App-Type': 'node',
                    'X-App-Name': appName,
                    'X-App-Version': appVersion,
                    'X-Platform-Info': platformInfo
                }
            });
            
            if (response.ok) {
                console.log('Health check sent successfully');
            } else {
                console.warn(`Health check failed: ${response.status}`);
            }
        } catch (error) {
            console.error('Error sending health check:', error);
        }
    }
}
```

#### JavaScript (Web Client - React)

```javascript
import { useEffect } from 'react';

export function useHealthCheck(pdvUrl) {
    useEffect(() => {
        const sendHealthCheck = async () => {
            const appId = localStorage.getItem('app_id') || 
                         (() => {
                             const id = crypto.randomUUID();
                             localStorage.setItem('app_id', id);
                             return id;
                         })();
            
            try {
                const platformInfo = `Web/${navigator.userAgent.split(' ').pop()}`;
                
                const response = await fetch(`${pdvUrl}/health`, {
                    method: 'GET',
                    headers: {
                        'X-App-ID': appId,
                        'X-App-Type': 'client',
                        'X-App-Name': 'Dashboard Client',
                        'X-App-Version': '1.0.0',
                        'X-Platform-Info': platformInfo
                    }
                });
                
                if (!response.ok) {
                    console.warn(`Health check failed: ${response.status}`);
                }
            } catch (error) {
                console.debug('PDV health check unavailable');
            }
        };
        
        // Send on mount and periodically
        sendHealthCheck();
        const interval = setInterval(sendHealthCheck, 30 * 60 * 1000); // Every 30 minutes
        
        return () => clearInterval(interval);
    }, [pdvUrl]);
}
```

### PDV Health Check Registry

When the PDV receives a health check, it stores one record per unique `X-App-ID` with:

* `appId`: Unique identifier (from `X-App-ID` header)
* `appType`: Component type (from `X-App-Type` header)
* `appName`: Display name (from `X-App-Name` header)
* `appVersion`: Version (from `X-App-Version` header)
* `platformInfo`: Platform details (from `X-Platform-Info` header)
* `firstConnection`: Timestamp of first health check (never changes)
* `lastConnected`: Timestamp of most recent health check (updated on each check)
* `connectionCount`: Total number of health checks received (incremented on each check)

This data allows the PDV to:

* **Display Accurate Statistics:** Dashboard shows total components connected and those active in the last 24 hours
* **Monitor Availability:** Detect when components become unavailable
* **Maintain Audit Logs:** Track which components have accessed the PDV and when
* **Facilitate Debugging:** Users and developers can see which app versions are in use

---

## Adapter Development

See [Adapter Specification](../spec/Adapter-Spec-v1.md) for detailed requirements.

Key points:

* Must implement health check registration (see above)
* Should call `sendHealthCheck()` after successfully connecting to PDV
* Must sign all events with your adapter's private key
* Should handle consent and PII flags appropriately

---

## Node Development

See [Node Specification](../spec/Node-Spec-v1.md) for detailed requirements.

Key points:

* Must implement health check registration (see above)
* Should call `sendHealthCheck()` after successful PDV connectivity tests
* Must subscribe to event topics appropriately
* Must sign all outputs (atoms, relations, metrics)
* Should implement idempotent processing

---

## Client Development

See [Client Specification](../spec/Client-Spec-v1.md) for detailed requirements.

Key points:

* SHOULD implement health check registration (optional but recommended)
* Must respect read-only constraints (no write operations)
* Must handle consent and redaction
* Should implement proper error handling for network issues
* Should respect rate limiting (429 responses with Retry-After headers)

---

## Best Practices

### 1. Health Check Error Handling

Health checks should **never block** your component's operation:

```kotlin
// Good: Async, non-blocking
scope.launch {
    try {
        healthCheckClient.sendHealthCheck(appId, appName, version)
    } catch (e: Exception) {
        Log.w("HealthCheck", "Failed to send, continuing anyway", e)
    }
}

// Bad: Blocking the main thread
healthCheckClient.sendHealthCheck(appId, appName, version) // Blocks!
```

### 2. Batching Health Checks

Avoid sending health checks too frequently. Batch with other PDV operations:

```kotlin
fun onPdvOperationSuccess() {
    // After successfully connecting for another operation,
    // also send a health check
    healthCheckClient.sendHealthCheck(appId, appName, version)
}
```

### 3. Timeout Configuration

Set appropriate timeouts for health checks (they're lightweight):

```kotlin
val httpClient = OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)
    .build()
```

### 4. Displaying App ID to Users

Make the app ID visible in your component's settings/about screen:

```kotlin
// Android
val appId = BuildConfig.APP_ID
val settingsText = "App ID: $appId"

// JavaScript
const appId = localStorage.getItem('app_id');
console.log(`App ID: ${appId}`);
```

---

## Testing & Debugging

### Manual Health Check Testing

You can test health checks manually using `curl`:

```bash
curl -i -X GET http://127.0.0.1:7777/health \
  -H "X-App-ID: $(uuidgen)" \
  -H "X-App-Type: adapter" \
  -H "X-App-Name: TestAdapter" \
  -H "X-App-Version: 1.0.0" \
  -H "X-Platform-Info: Test"
```

Expected response:

```
HTTP/1.1 200 OK
Content-Type: application/json

{"status":"healthy"}
```

### Viewing Registered Components

Check the PDV statistics endpoint to see all registered components:

```bash
curl http://127.0.0.1:7777/stats | jq .
```

Response includes component counts:

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

### Common Issues

**Issue:** Health checks failing with connection timeouts
- **Solution:** Ensure the PDV is running on the correct host/port (default: `http://127.0.0.1:7777`)

**Issue:** Health checks succeed but don't appear in stats
- **Solution:** Wait a few seconds and refresh stats; ensure proper header names (case-sensitive: `X-App-ID`, not `x-app-id`)

**Issue:** Same component showing as multiple entries in stats
- **Solution:** Use a consistent `X-App-ID` across builds; implement ID persistence (see ID generation examples above)

