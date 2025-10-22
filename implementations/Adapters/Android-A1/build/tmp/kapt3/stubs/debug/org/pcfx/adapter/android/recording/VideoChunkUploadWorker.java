package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\n\u001a\u00020\u000bH\u0016J\"\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u0014\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u000e\u0010\u0014\u001a\u00020\u000bH\u0082@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u000bH\u0002J\b\u0010\u001b\u001a\u00020\u000bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoChunkUploadWorker;", "Landroid/app/Service;", "<init>", "()V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "pdvClient", "Lorg/pcfx/adapter/android/network/PDVClient;", "isRunning", "", "onCreate", "", "onStartCommand", "", "intent", "Landroid/content/Intent;", "flags", "startId", "onBind", "Landroid/os/IBinder;", "runUploadLoop", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadChunkToBlob", "chunkFile", "Ljava/io/File;", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createNotificationChannel", "showUploadNotification", "Companion", "Android-A1_debug"})
public final class VideoChunkUploadWorker extends android.app.Service {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    private org.pcfx.adapter.android.network.PDVClient pdvClient;
    private boolean isRunning = false;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "VideoChunkUploadWorker";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "pcfx_chunk_upload_channel";
    private static final int NOTIFICATION_ID = 3;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START_UPLOAD = "org.pcfx.adapter.android.START_CHUNK_UPLOAD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP_UPLOAD = "org.pcfx.adapter.android.STOP_CHUNK_UPLOAD";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.recording.VideoChunkUploadWorker.Companion Companion = null;
    
    public VideoChunkUploadWorker() {
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
    
    private final java.lang.Object runUploadLoop(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object uploadChunkToBlob(java.io.File chunkFile, kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final void createNotificationChannel() {
    }
    
    private final void showUploadNotification() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoChunkUploadWorker$Companion;", "", "<init>", "()V", "TAG", "", "CHANNEL_ID", "NOTIFICATION_ID", "", "ACTION_START_UPLOAD", "ACTION_STOP_UPLOAD", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}