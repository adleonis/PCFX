package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u000b\u0018\u0000 %2\u00020\u0001:\u0001%B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\b\u0010\tJ\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0006\u0010\u0015\u001a\u00020\rJ\b\u0010\u0016\u001a\u0004\u0018\u00010\rJ\u0010\u0010\u0017\u001a\u00020\u00142\b\b\u0002\u0010\u0018\u001a\u00020\u0019J\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\u001bJ\u000e\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0019J\u000e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0019J\u000e\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u0007J\u0006\u0010!\u001a\u00020\u0007J\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\r0\u001bJ\u0010\u0010#\u001a\u0004\u0018\u00010\r2\u0006\u0010\u001d\u001a\u00020\u0019J\u0006\u0010$\u001a\u00020\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\r0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoChunkManager;", "", "context", "Landroid/content/Context;", "recordingDir", "Ljava/io/File;", "targetChunkDurationMs", "", "<init>", "(Landroid/content/Context;Ljava/io/File;J)V", "prefs", "Landroid/content/SharedPreferences;", "currentChunk", "Lorg/pcfx/adapter/android/recording/VideoChunk;", "chunkSequence", "Ljava/util/concurrent/atomic/AtomicLong;", "chunkList", "", "pendingUploadQueue", "ensureRecordingDir", "", "createNewChunk", "getCurrentChunk", "finalizeCurrentChunk", "frameCount", "", "getPendingChunks", "", "markChunkAsUploaded", "sequenceNumber", "deleteChunk", "cleanupOldChunks", "maxStorageBytes", "getTotalChunkSize", "getAllChunks", "getChunkBySequence", "resetForNewRecording", "Companion", "Android-A1_debug"})
public final class VideoChunkManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.File recordingDir = null;
    private final long targetChunkDurationMs = 0L;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.Nullable()
    private org.pcfx.adapter.android.recording.VideoChunk currentChunk;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicLong chunkSequence = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<org.pcfx.adapter.android.recording.VideoChunk> chunkList = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<org.pcfx.adapter.android.recording.VideoChunk> pendingUploadQueue = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "VideoChunkManager";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREF_CHUNK_SEQUENCE = "chunk_sequence";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.recording.VideoChunkManager.Companion Companion = null;
    
    public VideoChunkManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.io.File recordingDir, long targetChunkDurationMs) {
        super();
    }
    
    private final void ensureRecordingDir() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.recording.VideoChunk createNewChunk() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.pcfx.adapter.android.recording.VideoChunk getCurrentChunk() {
        return null;
    }
    
    public final void finalizeCurrentChunk(int frameCount) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.pcfx.adapter.android.recording.VideoChunk> getPendingChunks() {
        return null;
    }
    
    public final void markChunkAsUploaded(int sequenceNumber) {
    }
    
    public final void deleteChunk(int sequenceNumber) {
    }
    
    public final void cleanupOldChunks(long maxStorageBytes) {
    }
    
    public final long getTotalChunkSize() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.pcfx.adapter.android.recording.VideoChunk> getAllChunks() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.pcfx.adapter.android.recording.VideoChunk getChunkBySequence(int sequenceNumber) {
        return null;
    }
    
    public final void resetForNewRecording() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoChunkManager$Companion;", "", "<init>", "()V", "TAG", "", "PREF_CHUNK_SEQUENCE", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}