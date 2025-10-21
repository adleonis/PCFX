# Video Recording Integration Summary

## ✅ Integration Complete

The video recording system has been fully integrated with the PCF-X Android adapter event system and UI. Video recording events now automatically appear in the debug event log.

## 🎯 What Was Implemented

### 1. Event Integration ✅
- **VideoRecordingEventManager** - Manages video recording event emission
  - Emits "recording started" events when recording begins
  - Emits "recording stopped" events when recording ends (with duration)
  - Events respect consent workflow (skipped if no active consent)
  - Events stored in AppDatabase and published via EventPublisherService

### 2. EventBuilder Extensions ✅
- **buildVideoRecordingStartEvent()** - Creates event when recording starts
  - Surface: "video"
  - Kind: "video"
  - Content: File path of recording
  - Capabilities: ["screen.video.record"]
  
- **buildVideoRecordingStopEvent()** - Creates event when recording stops
  - Surface: "video"
  - Kind: "video"
  - Content: File path + duration in seconds
  - Capabilities: ["screen.video.record"]

### 3. UI Integration ✅
- **Start Recording / Stop Recording button** added to main app screen (ConsentActivity)
  - Red button with clear labeling
  - Only enabled when consent is granted
  - Toggles between "Start Recording" and "Stop Recording" states
  - Shows toast warning if consent not granted

### 4. Consent Workflow Integration ✅
- Video recording respects existing consent system
- Recording disabled until user grants consent
- Button state updated in real-time based on consent status
- Button disabled/enabled in onResume() to catch consent changes

### 5. VideoRecorderService Enhancements ✅
- Integrated VideoRecordingEventManager
- Tracks recording start time for duration calculation
- Emits events via event system on start/stop
- Events flow to debug page automatically

## 📊 Event Flow

```
User clicks "Start Recording" button
         ↓
ConsentActivity.toggleVideoRecording()
         ↓
VideoRecordingHelper.requestScreenCapturePermission()
         ↓
User grants permission in system dialog
         ↓
ConsentActivity.onActivityResult()
         ↓
VideoRecordingHelper.handleScreenCaptureResult()
         ↓
VideoRecorderService.startRecording()
         ↓
VideoRecordingEventManager.emitRecordingStartEvent()
         ↓
EventBuilder.buildVideoRecordingStartEvent()
         ↓
Event stored in AppDatabase
         ↓
EventPublisherService publishes event
         ↓
DebugExposureEventsActivity shows event (last 5)
```

## 📱 User Interface

### Main Screen (ConsentActivity)
- New red button: "Start Recording" / "Stop Recording"
- Button is:
  - **Disabled** when no consent is granted (labeled "Start Recording (Require Consent)")
  - **Enabled** when consent is granted
  - **States**: "Start Recording" → "Stop Recording" (while recording)

### Debug Page (DebugExposureEventsActivity)
- Video recording events appear automatically
- Event details include:
  - Event type: "video"
  - Surface: "video"
  - File path of recording
  - Duration (for stop events)
  - Timestamp and consent ID
  - Capabilities used: "screen.video.record"

## 🔄 How It Works

### Step 1: User Grants Consent
- ConsentActivity displays consent request
- User clicks "Accept Consent"
- Consent Manager saves consent
- Start Recording button becomes enabled

### Step 2: User Starts Recording
- User clicks "Start Recording" button
- System asks for screen capture permission
- User grants permission in system dialog
- VideoRecorderService starts recording
- **Recording Started event created** (stored in database)
- Button changes to "Stop Recording"
- Foreground notification shows "Recording video"

### Step 3: Recording Active
- Screen capture continues until user stops or phone sleeps
- Recording pauses automatically when screen turns off
- Recording resumes when screen turns back on
- File stored in `/cache/recordings/recording_YYYY-MM-DD_HH-mm-ss.mp4`

### Step 4: User Stops Recording
- User clicks "Stop Recording" button
- Recording thread stops
- VideoEncoder flushes and closes MP4 file
- **Recording Stopped event created** (with duration) (stored in database)
- Button changes back to "Start Recording"
- Notification dismissed

### Step 5: Events Appear in Debug Page
- Events are automatically fetched from database
- DebugExposureEventsActivity.loadEvents() displays last 5 events
- Video recording events show with:
  - File path and duration
  - Timestamps
  - "screen.video.record" capability marker

## 🔐 Consent Integration

The video recording system respects the existing consent workflow:

```kotlin
// In VideoRecordingEventManager
val consent = consentManager.getActiveConsent()
if (consent == null) {
    Log.d("VideoRecordingEventManager", "No active consent, skipping recording start event")
    return@launch
}
```

If user revokes consent:
- Recording button becomes disabled
- Recording is stopped if active
- New events are not created until consent is re-granted

## 📁 Modified Files

### New Files
1. `src/main/kotlin/org/pcfx/adapter/android/recording/VideoRecordingEventManager.kt` (101 lines)

### Modified Files
1. **VideoRecorderService.kt** - Added event emission on start/stop
2. **EventBuilder.kt** - Added buildVideoRecordingStartEvent() and buildVideoRecordingStopEvent()
3. **ConsentActivity.kt** - Added Start/Stop Recording button and handlers
4. **activity_consent.xml** - Added video recording button UI

## 🧪 Testing the Integration

### To Test:
1. Open the app
2. Click "Accept Consent"
3. Click "Start Recording" button
4. Grant screen capture permission when prompted
5. Verify notification shows "Recording video"
6. Click "View Exposure Events (Debug)"
7. **Video recording events should appear** at the top of the event list
8. Event details should show file path (e.g., `/data/data/.../cache/recordings/recording_2024-01-15_14-30-45.mp4`)
9. Return to main screen
10. Click "Stop Recording"
11. Return to debug page
12. **Recording stop event should appear** with duration

### Expected Events in Debug Page:
- Event type: "video"
- Surface: "video"
- Content includes file path
- Capabilities: ["screen.video.record"]
- Status: Pending (unposted initially), then Posted after sync with backend

## 🔗 Integration Points

### Event System Integration
- ✅ EventBuilder extended with video recording methods
- ✅ VideoRecordingEventManager handles event creation
- ✅ Events stored in AppDatabase (EventEntity)
- ✅ EventPublisherService publishes automatically
- ✅ Events appear in DebugExposureEventsActivity

### Consent System Integration
- ✅ Video recording respects active consent
- ✅ Button disabled without consent
- ✅ Events skipped if no consent
- ✅ Button state updates on consent changes

### Service Integration
- ✅ VideoRecorderService emits start/stop events
- ✅ VideoRecordingEventManager manages event details
- ✅ Duration tracked and included in stop event

### UI Integration
- ✅ Start/Stop button on main screen
- ✅ Button state reflects recording status
- ✅ Consent requirement enforced in UI
- ✅ Events auto-appear in debug page

## ✨ Features

- ✅ Automatic event creation on recording start/stop
- ✅ Duration tracking for stop events
- ✅ Consent-aware event emission
- ✅ Integration with existing event system
- ✅ Events stored locally in database
- ✅ Events published to backend
- ✅ Automatic appearance in debug event log
- ✅ Real-time button state updates
- ✅ Clear user feedback (toasts, button labels)

## 📝 Notes

- Video recording events use existing EventBuilder and event storage infrastructure
- No new database tables required - uses existing EventEntity table
- Events published via existing EventPublisherService
- Debug page automatically displays events without modifications
- Event retention follows consent manifest rules (default: 30 days)
- Events include full metadata (consent ID, device, timestamp, signature)

## 🚀 Ready for Production

All integration points are complete:
- ✅ Event creation system
- ✅ UI controls
- ✅ Consent integration
- ✅ Event storage and publishing
- ✅ Debug page display
- ✅ Error handling
- ✅ Logging

The video recording feature is fully integrated and ready for testing and production use.
