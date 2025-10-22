package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000  2\u00020\u0001:\u0001 B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0004\b\f\u0010\rJ\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0018\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0006\u0010\u001c\u001a\u00020\u0018J\u0006\u0010\u001d\u001a\u00020\u0018J\u0006\u0010\u001e\u001a\u00020\u0018J\u0006\u0010\u001f\u001a\u00020\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lorg/pcfx/adapter/android/recording/RecordingThread;", "Ljava/lang/Thread;", "recordingDir", "Ljava/io/File;", "mediaProjection", "Landroid/media/projection/MediaProjection;", "config", "Lorg/pcfx/adapter/android/recording/RecordingConfig;", "stateManager", "Lorg/pcfx/adapter/android/recording/RecordingStateManager;", "context", "Landroid/content/Context;", "<init>", "(Ljava/io/File;Landroid/media/projection/MediaProjection;Lorg/pcfx/adapter/android/recording/RecordingConfig;Lorg/pcfx/adapter/android/recording/RecordingStateManager;Landroid/content/Context;)V", "isRunning", "Ljava/util/concurrent/atomic/AtomicBoolean;", "isPaused", "pauseLock", "Ljava/lang/Object;", "chunkManager", "Lorg/pcfx/adapter/android/recording/VideoChunkManager;", "targetFramesPerChunk", "", "run", "", "finalizeChunkAndStartNew", "encoder", "Lorg/pcfx/adapter/android/recording/VideoEncoder;", "stopRecording", "pauseRecording", "resumeRecording", "getChunkManager", "Companion", "Android-A1_debug"})
public final class RecordingThread extends java.lang.Thread {
    @org.jetbrains.annotations.NotNull()
    private final java.io.File recordingDir = null;
    @org.jetbrains.annotations.NotNull()
    private final android.media.projection.MediaProjection mediaProjection = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.RecordingConfig config = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.RecordingStateManager stateManager = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicBoolean isRunning = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicBoolean isPaused = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object pauseLock = null;
    private org.pcfx.adapter.android.recording.VideoChunkManager chunkManager;
    private final int targetFramesPerChunk = 0;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "RecordingThread";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.recording.RecordingThread.Companion Companion = null;
    
    public RecordingThread(@org.jetbrains.annotations.NotNull()
    java.io.File recordingDir, @org.jetbrains.annotations.NotNull()
    android.media.projection.MediaProjection mediaProjection, @org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.recording.RecordingConfig config, @org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.recording.RecordingStateManager stateManager, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    public void run() {
    }
    
    private final void finalizeChunkAndStartNew(org.pcfx.adapter.android.recording.VideoEncoder encoder, org.pcfx.adapter.android.recording.VideoChunkManager chunkManager) {
    }
    
    public final void stopRecording() {
    }
    
    public final void pauseRecording() {
    }
    
    public final void resumeRecording() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.recording.VideoChunkManager getChunkManager() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/recording/RecordingThread$Companion;", "", "<init>", "()V", "TAG", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}