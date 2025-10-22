package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u0000 02\u00020\u0001:\u000201B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\"\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\u001dH\u0016J\u0014\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010$\u001a\u00020\u001bH\u0016J\u000e\u0010%\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010&J\b\u0010'\u001a\u00020\u001bH\u0002J\b\u0010(\u001a\u00020\u001bH\u0002J\b\u0010)\u001a\u00020\u001bH\u0002J\b\u0010*\u001a\u00020\u001bH\u0002J\b\u0010+\u001a\u00020\u001bH\u0002J\b\u0010,\u001a\u00020\u001bH\u0002J\b\u0010-\u001a\u00020\u001bH\u0002J\b\u0010.\u001a\u00020\u001bH\u0002J\b\u0010/\u001a\u00020\u001bH\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0014\u001a\u00060\u0015R\u00020\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecorderService;", "Landroid/app/Service;", "<init>", "()V", "recordingThread", "Lorg/pcfx/adapter/android/recording/RecordingThread;", "stateManager", "Lorg/pcfx/adapter/android/recording/RecordingStateManager;", "storageManager", "Lorg/pcfx/adapter/android/recording/StorageManager;", "config", "Lorg/pcfx/adapter/android/recording/RecordingConfig;", "eventManager", "Lorg/pcfx/adapter/android/recording/VideoRecordingEventManager;", "mediaProjectionManager", "Landroid/media/projection/MediaProjectionManager;", "mediaProjection", "Landroid/media/projection/MediaProjection;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "screenStateReceiver", "Lorg/pcfx/adapter/android/recording/VideoRecorderService$ScreenStateReceiver;", "recordingStartTime", "", "currentOutputFile", "Ljava/io/File;", "onCreate", "", "onStartCommand", "", "intent", "Landroid/content/Intent;", "flags", "startId", "onBind", "Landroid/os/IBinder;", "onDestroy", "startRecording", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stopRecording", "pauseRecording", "resumeRecording", "startChunkUploadWorker", "stopChunkUploadWorker", "createNotificationChannel", "showRecordingNotification", "registerScreenStateReceiver", "unregisterScreenStateReceiver", "Companion", "ScreenStateReceiver", "Android-A1_debug"})
public final class VideoRecorderService extends android.app.Service {
    @org.jetbrains.annotations.Nullable()
    private org.pcfx.adapter.android.recording.RecordingThread recordingThread;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.RecordingStateManager stateManager = null;
    private org.pcfx.adapter.android.recording.StorageManager storageManager;
    private org.pcfx.adapter.android.recording.RecordingConfig config;
    private org.pcfx.adapter.android.recording.VideoRecordingEventManager eventManager;
    @org.jetbrains.annotations.Nullable()
    private android.media.projection.MediaProjectionManager mediaProjectionManager;
    @org.jetbrains.annotations.Nullable()
    private android.media.projection.MediaProjection mediaProjection;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.VideoRecorderService.ScreenStateReceiver screenStateReceiver = null;
    private long recordingStartTime = 0L;
    @org.jetbrains.annotations.Nullable()
    private java.io.File currentOutputFile;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START_RECORDING = "org.pcfx.adapter.android.START_RECORDING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP_RECORDING = "org.pcfx.adapter.android.STOP_RECORDING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_PAUSE_RECORDING = "org.pcfx.adapter.android.PAUSE_RECORDING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_RESUME_RECORDING = "org.pcfx.adapter.android.RESUME_RECORDING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_MEDIA_PROJECTION = "org.pcfx.adapter.android.MEDIA_PROJECTION";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_RESULT_CODE = "org.pcfx.adapter.android.RESULT_CODE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PROJECTION_DATA = "org.pcfx.adapter.android.PROJECTION_DATA";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "pcfx_video_recording_channel";
    public static final int NOTIFICATION_ID = 2;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.ref.WeakReference<android.media.projection.MediaProjection> mediaProjectionHolder;
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.recording.VideoRecorderService.Companion Companion = null;
    
    public VideoRecorderService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final java.lang.Object startRecording(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void stopRecording() {
    }
    
    private final void pauseRecording() {
    }
    
    private final void resumeRecording() {
    }
    
    private final void startChunkUploadWorker() {
    }
    
    private final void stopChunkUploadWorker() {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final void showRecordingNotification() {
    }
    
    private final void registerScreenStateReceiver() {
    }
    
    private final void unregisterScreenStateReceiver() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0011J\b\u0010\u0015\u001a\u0004\u0018\u00010\u0011J\u0006\u0010\u0016\u001a\u00020\u0013R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0086T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecorderService$Companion;", "", "<init>", "()V", "ACTION_START_RECORDING", "", "ACTION_STOP_RECORDING", "ACTION_PAUSE_RECORDING", "ACTION_RESUME_RECORDING", "EXTRA_MEDIA_PROJECTION", "EXTRA_RESULT_CODE", "EXTRA_PROJECTION_DATA", "CHANNEL_ID", "NOTIFICATION_ID", "", "mediaProjectionHolder", "Ljava/lang/ref/WeakReference;", "Landroid/media/projection/MediaProjection;", "setMediaProjection", "", "projection", "getMediaProjection", "clearMediaProjection", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void setMediaProjection(@org.jetbrains.annotations.NotNull()
        android.media.projection.MediaProjection projection) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final android.media.projection.MediaProjection getMediaProjection() {
            return null;
        }
        
        public final void clearMediaProjection() {
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016\u00a8\u0006\n"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecorderService$ScreenStateReceiver;", "Landroid/content/BroadcastReceiver;", "<init>", "(Lorg/pcfx/adapter/android/recording/VideoRecorderService;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "Android-A1_debug"})
    final class ScreenStateReceiver extends android.content.BroadcastReceiver {
        
        public ScreenStateReceiver() {
            super();
        }
        
        @java.lang.Override()
        public void onReceive(@org.jetbrains.annotations.Nullable()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
        }
    }
}