package org.pcfx.adapter.android.service;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0012H\u0016J\u0010\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J \u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\t2\b\u0010\u001a\u001a\u0004\u0018\u00010\tH\u0082@\u00a2\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u001dH\u0002J\u0010\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lorg/pcfx/adapter/android/service/AccessibilityMonitorService;", "Landroid/accessibilityservice/AccessibilityService;", "<init>", "()V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "gson", "Lcom/google/gson/Gson;", "lastPackage", "", "currentForegroundApp", "eventCountByType", "", "", "eventCountTotal", "sessionStartTime", "", "onServiceConnected", "", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onInterrupt", "handleWindowStateChanged", "publishAppFocusEvent", "packageName", "windowTitle", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isConsentGranted", "", "isAccessibilityRecordingEnabled", "getEventTypeName", "eventType", "Android-A1_debug"})
public final class AccessibilityMonitorService extends android.accessibilityservice.AccessibilityService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastPackage;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentForegroundApp;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.String, java.lang.Integer> eventCountByType;
    private int eventCountTotal = 0;
    private long sessionStartTime;
    
    public AccessibilityMonitorService() {
        super();
    }
    
    @java.lang.Override()
    protected void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onAccessibilityEvent(@org.jetbrains.annotations.NotNull()
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    @java.lang.Override()
    public void onInterrupt() {
    }
    
    private final void handleWindowStateChanged(android.view.accessibility.AccessibilityEvent event) {
    }
    
    private final java.lang.Object publishAppFocusEvent(java.lang.String packageName, java.lang.String windowTitle, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final boolean isConsentGranted() {
        return false;
    }
    
    private final boolean isAccessibilityRecordingEnabled() {
        return false;
    }
    
    private final java.lang.String getEventTypeName(int eventType) {
        return null;
    }
}