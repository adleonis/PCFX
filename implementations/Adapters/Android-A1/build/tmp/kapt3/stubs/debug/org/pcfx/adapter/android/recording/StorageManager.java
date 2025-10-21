package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\f\u001a\u00020\tJ\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u000eJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\tJ\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\u0017J\u0010\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u000eH\u0002J\u0006\u0010\u001a\u001a\u00020\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lorg/pcfx/adapter/android/recording/StorageManager;", "", "context", "Landroid/content/Context;", "config", "Lorg/pcfx/adapter/android/recording/RecordingConfig;", "<init>", "(Landroid/content/Context;Lorg/pcfx/adapter/android/recording/RecordingConfig;)V", "recordingDir", "Ljava/io/File;", "dateFormat", "Ljava/text/SimpleDateFormat;", "getNextOutputFile", "getTotalUsedSpace", "", "getAvailableSpace", "ensureSpace", "", "cleanup", "", "deleteFile", "file", "getAllRecordingFiles", "", "cleanupOldestFiles", "targetSize", "getRecordingDirPath", "", "Android-A1_debug"})
public final class StorageManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.RecordingConfig config = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.File recordingDir = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat dateFormat = null;
    
    public StorageManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.recording.RecordingConfig config) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File getNextOutputFile() {
        return null;
    }
    
    public final long getTotalUsedSpace() {
        return 0L;
    }
    
    public final long getAvailableSpace() {
        return 0L;
    }
    
    public final boolean ensureSpace() {
        return false;
    }
    
    public final void cleanup() {
    }
    
    public final boolean deleteFile(@org.jetbrains.annotations.NotNull()
    java.io.File file) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.io.File> getAllRecordingFiles() {
        return null;
    }
    
    private final void cleanupOldestFiles(long targetSize) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRecordingDirPath() {
        return null;
    }
}