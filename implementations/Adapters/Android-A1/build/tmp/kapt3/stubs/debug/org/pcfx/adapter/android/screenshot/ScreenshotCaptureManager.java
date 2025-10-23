package org.pcfx.adapter.android.screenshot;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\"\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u000eJ\u0006\u0010\u0012\u001a\u00020\fJ\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u000eJ\u0006\u0010\u0015\u001a\u00020\u000eJ\u0006\u0010\u0016\u001a\u00020\tJ\u0010\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u000eH\u0002J\u0010\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\tH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotCaptureManager;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "isCapturing", "", "lock", "startCapture", "", "intervalSeconds", "", "consentId", "", "retentionDays", "stopCapture", "setInterval", "seconds", "getInterval", "isEnabled", "saveInterval", "setEnabled", "enabled", "Companion", "Android-A1_debug"})
public final class ScreenshotCaptureManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    private boolean isCapturing = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object lock = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ScreenshotCaptureManager";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_INTERVAL = "screenshot_interval_seconds";
    private static final int PREFS_DEFAULT_INTERVAL = 2;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_ENABLED = "screenshot_enabled";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.screenshot.ScreenshotCaptureManager.Companion Companion = null;
    
    public ScreenshotCaptureManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void startCapture(int intervalSeconds, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, int retentionDays) {
    }
    
    public final void stopCapture() {
    }
    
    public final boolean isCapturing() {
        return false;
    }
    
    public final void setInterval(int seconds) {
    }
    
    public final int getInterval() {
        return 0;
    }
    
    public final boolean isEnabled() {
        return false;
    }
    
    private final void saveInterval(int seconds) {
    }
    
    private final void setEnabled(boolean enabled) {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotCaptureManager$Companion;", "", "<init>", "()V", "TAG", "", "PREFS_INTERVAL", "PREFS_DEFAULT_INTERVAL", "", "PREFS_ENABLED", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}