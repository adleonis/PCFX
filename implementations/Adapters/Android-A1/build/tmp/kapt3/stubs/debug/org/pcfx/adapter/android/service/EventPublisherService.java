package org.pcfx.adapter.android.service;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000  2\u00020\u0001:\u0001 B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\"\u0010\u0015\u001a\u00020\u000f2\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u000fH\u0016J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u000e\u0010\u001c\u001a\u00020\u0014H\u0082@\u00a2\u0006\u0002\u0010\u001dJ\u000e\u0010\u001e\u001a\u00020\u0014H\u0082@\u00a2\u0006\u0002\u0010\u001dJ\b\u0010\u001f\u001a\u00020\u0014H\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lorg/pcfx/adapter/android/service/EventPublisherService;", "Landroid/app/Service;", "<init>", "()V", "serviceJob", "Lkotlinx/coroutines/Job;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "pdvClient", "Lorg/pcfx/adapter/android/network/PDVClient;", "db", "Lorg/pcfx/adapter/android/db/AppDatabase;", "gson", "Lcom/google/gson/Gson;", "eventCountPublished", "", "eventCountFailed", "sessionStartTime", "", "onCreate", "", "onStartCommand", "intent", "Landroid/content/Intent;", "flags", "startId", "onBind", "Landroid/os/IBinder;", "publishQueuedEvents", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stopSelfIfNoMoreEvents", "createNotificationChannel", "Companion", "Android-A1_debug"})
public final class EventPublisherService extends android.app.Service {
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job serviceJob;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    private org.pcfx.adapter.android.network.PDVClient pdvClient;
    private org.pcfx.adapter.android.db.AppDatabase db;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    private int eventCountPublished = 0;
    private int eventCountFailed = 0;
    private long sessionStartTime;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_PUBLISH_QUEUED_EVENTS = "org.pcfx.adapter.android.PUBLISH_QUEUED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FLUSH_NOW = "org.pcfx.adapter.android.FLUSH_NOW";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "pcfx_adapter_channel";
    public static final int NOTIFICATION_ID = 1;
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.service.EventPublisherService.Companion Companion = null;
    
    public EventPublisherService() {
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
    
    private final java.lang.Object publishQueuedEvents(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object stopSelfIfNoMoreEvents(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void createNotificationChannel() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lorg/pcfx/adapter/android/service/EventPublisherService$Companion;", "", "<init>", "()V", "ACTION_PUBLISH_QUEUED_EVENTS", "", "ACTION_FLUSH_NOW", "CHANNEL_ID", "NOTIFICATION_ID", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}