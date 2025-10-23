package org.pcfx.adapter.android.screenshot;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0005\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J*\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\rJ&\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00122\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0015\u001a\u00020\rH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotEventBuilder;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "eventBuilder", "Lorg/pcfx/adapter/android/event/EventBuilder;", "buildScreenshotTextEvent", "Lorg/pcfx/adapter/android/model/ExposureEvent;", "ocrResult", "Lorg/pcfx/adapter/android/screenshot/OCRResult;", "consentId", "", "retentionDays", "", "previousEventId", "buildExtensions", "", "signEvent", "event", "getDeviceIdentifier", "Companion", "Android-A1_debug"})
public final class ScreenshotEventBuilder {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.event.EventBuilder eventBuilder = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ScreenshotEventBuilder";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.screenshot.ScreenshotEventBuilder.Companion Companion = null;
    
    public ScreenshotEventBuilder(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.model.ExposureEvent buildScreenshotTextEvent(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.screenshot.OCRResult ocrResult, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, int retentionDays, @org.jetbrains.annotations.Nullable()
    java.lang.String previousEventId) {
        return null;
    }
    
    private final java.util.Map<java.lang.String, java.lang.Object> buildExtensions(org.pcfx.adapter.android.screenshot.OCRResult ocrResult, java.lang.String previousEventId) {
        return null;
    }
    
    private final java.lang.String signEvent(org.pcfx.adapter.android.model.ExposureEvent event) {
        return null;
    }
    
    private final java.lang.String getDeviceIdentifier() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotEventBuilder$Companion;", "", "<init>", "()V", "TAG", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}