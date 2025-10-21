# Video Recording Implementation Guide

## Overview
This document describes the video recording system implemented for the PCF-X Android adapter. The system is optimized for battery, CPU, and storage efficiency while respecting user privacy and consent.

## Architecture

### Core Components

#### 1. VideoRecorderService
A foreground service that manages the lifecycle of video recording.

**Location**: `org.pcfx.adapter.android.recording.VideoRecorderService`

**Responsibilities**:
- Manages foreground service lifecycle
- Handles recording start/stop/pause/resume
- Monitors screen state and pauses recording when phone sleeps
- Shows persistent notification during recording

**Actions**:
- `ACTION_START_RECORDING`: Start video recording
- `ACTION_STOP_RECORDING`: Stop video recording
- `ACTION_PAUSE_RECORDING`: Pause video recording
- `ACTION_RESUME_RECORDING`: Resume video recording

#### 2. VideoEncoder
Hardware-accelerated H.264 video encoder using MediaCodec and MediaMuxer.

**Location**: `org.pcfx.adapter.android.recording.VideoEncoder`

**Features**:
- Hardware acceleration for efficient CPU usage
- H.264 codec (AVC) for compatibility
- MP4 container format (via MediaMuxer)
- Configurable bitrate and frame rate
- Proper frame synchronization

#### 3. ScreenCaptureManager
Manages screen capture using the MediaProjection API.

**Location**: `org.pcfx.adapter.android.recording.ScreenCaptureManager`

**Features**:
- Virtual display creation from MediaProjection
- Configurable resolution (default: 480x360)
- Display lifecycle management

#### 4. StorageManager
Manages local storage with rolling buffer cleanup.

**Location**: `org.pcfx.adapter.android.recording.StorageManager`

**Features**:
- Rolling buffer system with auto-cleanup
- Configurable maximum storage size (default: 50GB)
- Oldest-file-first deletion strategy
- Storage space monitoring

#### 5. RecordingThread
Dedicated thread for frame capture and encoding.

**Location**: `org.pcfx.adapter.android.recording.RecordingThread`

**Features**:
- Synchronous frame encoding
- Pause/resume support with proper synchronization
- Error handling and state management

#### 6. RecordingStateManager
Manages recording state and transitions.

**Location**: `org.pcfx.adapter.android.recording.RecordingStateManager`

**States**:
- `Idle`: Not recording
- `Recording`: Active recording
- `Paused`: Recording paused
- `Error`: Recording failed with error details

#### 7. RecordingConfig
Configuration data class for recording parameters.

**Location**: `org.pcfx.adapter.android.recording.RecordingConfig`

**Parameters**:
- `width`: 480 (default)
- `height`: 360 (default)
- `bitrate`: 2500000 bps (2.5 Mbps, default)
- `frameRate`: 15 fps (default)
- `maxStorageSizeBytes`: 50GB (default)
- `codec`: "video/avc" (H.264, default)
- `mimeType`: "video/mp4" (default)

## Integration Guide

### Basic Usage

```kotlin
import org.pcfx.adapter.android.recording.VideoRecordingHelper

// In your Activity
val recordingHelper = VideoRecordingHelper(this)

// Request screen capture permission
recordingHelper.requestScreenCapturePermission(this) { granted ->
    if (granted) {
        // Permission granted, proceed with recording
    }
}

// Handle result in onActivityResult
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == VideoRecordingHelper.REQUEST_MEDIA_PROJECTION) {
        recordingHelper.handleScreenCaptureResult(resultCode, data, this)
    }
}

// Stop recording when needed
recordingHelper.stopRecording()

// Pause recording
recordingHelper.pauseRecording()

// Resume recording
recordingHelper.resumeRecording()
```

### MediaProjection Permission

Users must grant screen capture permission explicitly. This is handled via:

1. Request intent from `MediaProjectionManager.createScreenCaptureIntent()`
2. User confirms permission in system dialog
3. App receives `MediaProjection` object to use for capturing

### Lifecycle Integration

The recording service:
- Starts when app requests video recording
- Pauses automatically when screen turns off
- Resumes automatically when screen turns on
- Stops when explicitly requested or app closes

### Storage Details

Videos are stored in:
- Location: `<app-cache-dir>/recordings/`
- Format: `recording_YYYY-MM-DD_HH-mm-ss.mp4`
- Automatic cleanup: Oldest files deleted when storage exceeds 50GB limit

## Performance Characteristics

### Battery Impact
- **Hardware H.264 encoding**: Minimal CPU usage
- **15 fps @ 480x360**: Lower frame rate reduces power consumption
- **Foreground service**: Prevents background task killing

### CPU Impact
- **Hardware encoder**: Offloads to GPU/dedicated hardware
- **Dedicated thread**: Prevents main thread blocking
- **Frame draining**: Non-blocking encoder output handling

### Storage Impact
- **2.5 Mbps bitrate @ 15 fps**: ~1.1 GB per hour
- **50GB allocation**: Stores ~45 hours of video (with some overhead)
- **Auto-cleanup**: Maintains storage under limit automatically

### Network Impact
- **Local processing only**: No real-time network transmission
- **Stored locally**: Backend can retrieve files at convenience

## Permissions Required

The following permissions are declared in `AndroidManifest.xml`:

```xml
<!-- Video recording specific -->
<uses-permission android:name="android.permission.CAPTURE_VIDEO_OUTPUT" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />

<!-- Foreground service -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION" />
```

## Error Handling

The system handles the following error scenarios:

1. **Insufficient Storage**: Returns error if storage threshold reached
2. **Encoder Initialization Failure**: Logs error and reports to StateManager
3. **Virtual Display Creation Failure**: Gracefully stops recording
4. **Frame Encoding Failure**: Captured in RecordingState.Error

All errors are accessible via `RecordingStateManager.state` StateFlow.

## Testing

To test the implementation:

1. Build and run the app on a physical device (Android 8.0+)
2. Grant consent for recording
3. Request screen capture permission when prompted
4. Verify notification appears during recording
5. Check `/storage/emulated/0/Android/data/<app-package>/cache/recordings/` for video files
6. Verify pause/resume works (turn screen off/on)
7. Check file sizes match expected bitrate

## Notes

- **Audio**: Not currently recorded; can be added later
- **Resolution**: 480x360 for efficiency; adjustable via RecordingConfig
- **Frame Rate**: 15 fps balances smoothness and efficiency
- **Storage**: 50GB default; user-configurable
- **Codec**: H.264 (AVC) for broad device compatibility

## Future Enhancements

1. Audio recording support
2. Variable bitrate encoding
3. Different resolution options
4. Real-time streaming option
5. Event correlation with recording timestamps
