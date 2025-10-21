package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0018\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010J\u000e\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\tJ\u0006\u0010\u0015\u001a\u00020\tJ\u0006\u0010\u0016\u001a\u00020\tJ\u0010\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u0010H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecordingHelper;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "mediaProjectionManager", "Landroid/media/projection/MediaProjectionManager;", "requestScreenCapturePermission", "", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "handleScreenCaptureResult", "resultCode", "", "data", "Landroid/content/Intent;", "startRecording", "mediaProjection", "Landroid/media/projection/MediaProjection;", "stopRecording", "pauseRecording", "resumeRecording", "startForegroundService", "intent", "Companion", "Android-A1_debug"})
public final class VideoRecordingHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.media.projection.MediaProjectionManager mediaProjectionManager = null;
    public static final int REQUEST_MEDIA_PROJECTION = 1001;
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.recording.VideoRecordingHelper.Companion Companion = null;
    
    public VideoRecordingHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void requestScreenCapturePermission(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity) {
    }
    
    public final void handleScreenCaptureResult(int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    public final void startRecording(@org.jetbrains.annotations.NotNull()
    android.media.projection.MediaProjection mediaProjection) {
    }
    
    public final void stopRecording() {
    }
    
    public final void pauseRecording() {
    }
    
    public final void resumeRecording() {
    }
    
    private final void startForegroundService(android.content.Intent intent) {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecordingHelper$Companion;", "", "<init>", "()V", "REQUEST_MEDIA_PROJECTION", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}