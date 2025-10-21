package org.pcfx.adapter.android.event;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 $2\u00020\u0001:\u0001$B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J2\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u0013\u001a\u0004\u0018\u00010\r2\b\u0010\u0014\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J<\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\r0\u001aJ6\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\r0\u001aH\u0002J\u0010\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\u000bH\u0002J\b\u0010\"\u001a\u00020\rH\u0002J\u000e\u0010#\u001a\u00020\r2\u0006\u0010!\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lorg/pcfx/adapter/android/event/EventBuilder;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "keyManager", "Lorg/pcfx/adapter/android/security/KeyManager;", "gson", "Lcom/google/gson/Gson;", "buildAppFocusEvent", "Lorg/pcfx/adapter/android/model/ExposureEvent;", "packageName", "", "windowTitle", "consentId", "retentionDays", "", "buildNotificationEvent", "notificationTitle", "notificationText", "buildMediaEvent", "surface", "kind", "blobRef", "capabilitiesUsed", "", "createEvent", "source", "Lorg/pcfx/adapter/android/model/ExposureEvent$Source;", "content", "Lorg/pcfx/adapter/android/model/ExposureEvent$Content;", "signEvent", "event", "getDeviceIdentifier", "eventToJson", "Companion", "Android-A1_debug"})
public final class EventBuilder {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.security.KeyManager keyManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ADAPTER_ID = "org.pcfx.adapter.android/0.1.0";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.event.EventBuilder.Companion Companion = null;
    
    public EventBuilder(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.model.ExposureEvent buildAppFocusEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.Nullable()
    java.lang.String windowTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, int retentionDays) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.model.ExposureEvent buildNotificationEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.Nullable()
    java.lang.String notificationTitle, @org.jetbrains.annotations.Nullable()
    java.lang.String notificationText, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, int retentionDays) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.model.ExposureEvent buildMediaEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String surface, @org.jetbrains.annotations.NotNull()
    java.lang.String kind, @org.jetbrains.annotations.NotNull()
    java.lang.String blobRef, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, int retentionDays, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> capabilitiesUsed) {
        return null;
    }
    
    private final org.pcfx.adapter.android.model.ExposureEvent createEvent(org.pcfx.adapter.android.model.ExposureEvent.Source source, org.pcfx.adapter.android.model.ExposureEvent.Content content, java.lang.String consentId, int retentionDays, java.util.List<java.lang.String> capabilitiesUsed) {
        return null;
    }
    
    private final java.lang.String signEvent(org.pcfx.adapter.android.model.ExposureEvent event) {
        return null;
    }
    
    private final java.lang.String getDeviceIdentifier() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String eventToJson(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.model.ExposureEvent event) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/event/EventBuilder$Companion;", "", "<init>", "()V", "ADAPTER_ID", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}