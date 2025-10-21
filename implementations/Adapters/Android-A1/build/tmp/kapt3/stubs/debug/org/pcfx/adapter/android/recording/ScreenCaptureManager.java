package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0012J\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00150\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lorg/pcfx/adapter/android/recording/ScreenCaptureManager;", "", "context", "Landroid/content/Context;", "mediaProjection", "Landroid/media/projection/MediaProjection;", "config", "Lorg/pcfx/adapter/android/recording/RecordingConfig;", "<init>", "(Landroid/content/Context;Landroid/media/projection/MediaProjection;Lorg/pcfx/adapter/android/recording/RecordingConfig;)V", "displayManager", "Landroid/hardware/display/DisplayManager;", "virtualDisplay", "Landroid/hardware/display/VirtualDisplay;", "createVirtualDisplay", "surface", "Landroid/view/Surface;", "releaseVirtualDisplay", "", "getDisplayDimensions", "Lkotlin/Pair;", "", "Android-A1_debug"})
public final class ScreenCaptureManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.media.projection.MediaProjection mediaProjection = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.RecordingConfig config = null;
    @org.jetbrains.annotations.NotNull()
    private final android.hardware.display.DisplayManager displayManager = null;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.display.VirtualDisplay virtualDisplay;
    
    public ScreenCaptureManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.media.projection.MediaProjection mediaProjection, @org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.recording.RecordingConfig config) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.hardware.display.VirtualDisplay createVirtualDisplay(@org.jetbrains.annotations.NotNull()
    android.view.Surface surface) {
        return null;
    }
    
    public final void releaseVirtualDisplay() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Pair<java.lang.Integer, java.lang.Integer> getDisplayDimensions() {
        return null;
    }
}