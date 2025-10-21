package org.pcfx.adapter.android.service;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0016\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0082@\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lorg/pcfx/adapter/android/service/NotificationListenerService;", "Landroid/service/notification/NotificationListenerService;", "<init>", "()V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "gson", "Lcom/google/gson/Gson;", "onNotificationPosted", "", "sbn", "Landroid/service/notification/StatusBarNotification;", "onNotificationRemoved", "publishNotificationEvent", "(Landroid/service/notification/StatusBarNotification;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isConsentGranted", "", "Android-A1_debug"})
public final class NotificationListenerService extends android.service.notification.NotificationListenerService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    
    public NotificationListenerService() {
        super();
    }
    
    @java.lang.Override()
    public void onNotificationPosted(@org.jetbrains.annotations.NotNull()
    android.service.notification.StatusBarNotification sbn) {
    }
    
    @java.lang.Override()
    public void onNotificationRemoved(@org.jetbrains.annotations.NotNull()
    android.service.notification.StatusBarNotification sbn) {
    }
    
    private final java.lang.Object publishNotificationEvent(android.service.notification.StatusBarNotification sbn, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final boolean isConsentGranted() {
        return false;
    }
}