package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J \u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0006\u0010\u001d\u001a\u00020\u0011J\u001e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0082@\u00a2\u0006\u0002\u0010#J\u0016\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020&H\u0082@\u00a2\u0006\u0002\u0010'R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoRecordingEventManager;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "eventBuilder", "Lorg/pcfx/adapter/android/event/EventBuilder;", "gson", "Lcom/google/gson/Gson;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "chunkMonitorJob", "Lkotlinx/coroutines/Job;", "isMonitoring", "Ljava/util/concurrent/atomic/AtomicBoolean;", "emitRecordingStartEvent", "", "outputFilePath", "", "recordingThread", "Lorg/pcfx/adapter/android/recording/RecordingThread;", "emitRecordingStopEvent", "recordingDirPath", "durationSeconds", "", "totalChunks", "", "startChunkMonitor", "stopMonitoring", "emitChunkEvent", "chunk", "Lorg/pcfx/adapter/android/recording/VideoChunk;", "consent", "Lorg/pcfx/adapter/android/model/ConsentManifest;", "(Lorg/pcfx/adapter/android/recording/VideoChunk;Lorg/pcfx/adapter/android/model/ConsentManifest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "storeAndPublishEvent", "event", "Lorg/pcfx/adapter/android/model/ExposureEvent;", "(Lorg/pcfx/adapter/android/model/ExposureEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Android-A1_debug"})
public final class VideoRecordingEventManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.event.EventBuilder eventBuilder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job chunkMonitorJob;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicBoolean isMonitoring = null;
    
    public VideoRecordingEventManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void emitRecordingStartEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String outputFilePath, @org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.recording.RecordingThread recordingThread) {
    }
    
    public final void emitRecordingStopEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String recordingDirPath, long durationSeconds, int totalChunks) {
    }
    
    private final void startChunkMonitor(org.pcfx.adapter.android.recording.RecordingThread recordingThread) {
    }
    
    public final void stopMonitoring() {
    }
    
    private final java.lang.Object emitChunkEvent(org.pcfx.adapter.android.recording.VideoChunk chunk, org.pcfx.adapter.android.model.ConsentManifest consent, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object storeAndPublishEvent(org.pcfx.adapter.android.model.ExposureEvent event, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}