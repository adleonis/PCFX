# PDV-1 Quick Start Guide

## 5-Minute Setup

### 1. Build the App

```bash
cd implementations/PDV/PDV-1
./gradlew assembleDebug
```

### 2. Install on Emulator/Device

```bash
./gradlew installDebug
```

### 3. Start the App

1. Open PDV-1 app on your Android device/emulator
2. Tap **"Start Server"** button
3. Watch the status change to "PDV Server starting..."

### 4. Verify It's Running

```bash
# From your development machine
curl http://127.0.0.1:7777/stats
```

You should see:
```json
{
  "events": 0,
  "atoms": 0,
  "metrics": 0,
  "blobs": 0
}
```

## Testing with Android-A1 & Android-N1

### Setup Steps

1. **Start PDV-1 on emulator**
   - Launch PDV-1 app
   - Tap "Start Server"
   - Server runs on `http://127.0.0.1:7777`

2. **Test with Android-N1**
   - Press "Start Atomization" button
   - N1 will:
     - Fetch events from PDV-1 (`GET /events`)
     - Process them through DefaultAtomizer
     - Post atoms back to PDV-1 (`POST /atoms`)
   - Check PDV-1 /stats endpoint to see atoms count increase

3. **Test with Android-A1** (when ready)
   - Events are stored locally in A1
   - EventPublisherService POSTs to PDV-1 `/events`
   - Verify events appear in PDV-1 `/stats`

## API Quick Reference

### Get Statistics
```bash
curl http://127.0.0.1:7777/stats
```

### Fetch Events
```bash
curl "http://127.0.0.1:7777/events?since=1970-01-01T00:00:00Z&limit=64"
```

### Fetch Atoms
```bash
curl "http://127.0.0.1:7777/atoms?since=1970-01-01T00:00:00Z&limit=64"
```

### Post an Event
```bash
curl -X POST http://127.0.0.1:7777/events \
  -H "Content-Type: application/json" \
  -d '{
    "id": "event-123",
    "ts": "2025-10-21T20:00:00Z",
    "device": "emulator",
    "adapter_id": "test.adapter",
    "schema": "pcfx.exposure_event/0.1",
    "event": { "data": "test" }
  }'
```

### Post an Atom
```bash
curl -X POST http://127.0.0.1:7777/atoms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "atom-123",
    "ts": "2025-10-21T20:00:00Z",
    "node_id": "atomizer.node",
    "schema": "pcfx.knowledge_atom/0.1",
    "atom": { "data": "test atom" }
  }'
```

## Configuration

### Change Port
Use the SeekBar in the app (default: 7777, range: 7000-8000)

### Auto-Start on Boot
Enable the "Auto-start PDV Server on boot" checkbox

### View Logs
```bash
adb logcat | grep -E "PdvServer|EventRepository|AtomRepository"
```

## Troubleshooting

### Server Won't Start
1. Check Android logs: `adb logcat`
2. Ensure port is not in use (try different port via SeekBar)
3. Verify INTERNET permission is granted

### Connection Refused from N1/A1
1. Confirm PDV-1 says "Server running"
2. Test with curl from your machine
3. Check N1/A1 is configured for correct port (7777 default)

### No Data in /stats
1. Check logs for insert errors
2. Verify POST requests are being made
3. Check JSON format is valid

### App Crashes on Start
1. Clear app data: `adb shell pm clear org.pcfx.pdv.androidpdv1`
2. Reinstall: `./gradlew installDebug`
3. Check Android version (min SDK 26)

## Next Steps

1. **Test End-to-End Flow**
   - Start PDV-1
   - Run N1 atomization
   - Verify atoms appear in `/stats`

2. **Test Blob Upload**
   - POST to `/blobs` endpoint
   - Verify file is stored
   - Retrieve with GET `/blobs/{hash}`

3. **Test Consent Enforcement** (Phase 2)
   - POST consent records
   - Verify capability checking works

## Architecture Overview

```
PDV-1 (HTTP Server on 7777)
  ├── Room Database (SQLite)
  │   ├── events
  │   ├── atoms
  │   ├── metrics
  │   ├── blobs
  │   └── consents
  ├── REST Endpoints
  │   ├── /events (POST/GET)
  │   ├── /atoms (POST/GET)
  │   ├── /metrics (POST/GET)
  │   ├── /blobs (POST/GET)
  │   ├── /consents (POST/GET)
  │   └── /stats (GET)
  └── Android Service
      └── Foreground Service (always running)
```

## Files Structure
```
implementations/
  PDV/
    PDV-1/                          # ← You are here
      src/main/
        kotlin/org/pcfx/pdv/androidpdv1/
          data/           # Room entities & DAOs
          domain/         # Business logic
          server/         # Ktor HTTP server
          service/        # Android service
          receiver/       # Boot receiver
          ui/             # MainActivity
        res/              # Resources
        AndroidManifest.xml
      build.gradle.kts    # Dependencies & build config
      README.md           # Full documentation
```

## Production Deployment

### For Phase 3 (Cloud)
1. Extract API logic to shared library
2. Implement cloud PDV service
3. Update Adapters/Nodes to point to cloud endpoint
4. Add HTTPS/TLS
5. Implement authentication

### For On-Device Hardening
1. Add encryption at rest
2. Add signature verification
3. Enforce capability checks
4. Add audit logging
5. Implement rate limiting

---

**Ready to test?** Start the app and hit those API endpoints!

For detailed documentation, see [README.md](README.md) and [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)
