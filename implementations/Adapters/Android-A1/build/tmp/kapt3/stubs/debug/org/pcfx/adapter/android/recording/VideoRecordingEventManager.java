package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecordingEventManager;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "eventBuilder", "Lorg/pcfx/adapter/android/event/EventBuilder;", "gson", "Lcom/google/gson/Gson;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "emitRecordingStartEvent", "", "outputFilePath", "", "emitRecordingStopEvent", "durationSeconds", "", "storeAndPublishEvent", "event", "Lorg/pcfx/adapter/android/model/ExposureEvent;", "(Lorg/pcfx/adapter/android/model/ExposureEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Android-A1_debug"})
public final class VideoRecordingEventManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.event.EventBuilder eventBuilder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    
    public VideoRecordingEventManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void emitRecordingStartEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String outputFilePath) {
    }
    
    public final void emitRecordingStopEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String outputFilePath, long durationSeconds) {
    }
    
    private final java.lang.Object storeAndPublishEvent(org.pcfx.adapter.android.model.ExposureEvent event, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}