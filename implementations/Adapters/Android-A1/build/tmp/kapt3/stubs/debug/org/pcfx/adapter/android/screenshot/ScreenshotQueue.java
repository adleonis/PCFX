package org.pcfx.adapter.android.screenshot;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \u001f2\u00020\u0001:\u0002\u001e\u001fB\u0011\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nJ\u0014\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\n\u0018\u00010\u0013J\u0016\u0010\u0014\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\n\u0018\u00010\u0013H\u0002J\u0006\u0010\u0015\u001a\u00020\u0016J\u0006\u0010\u0017\u001a\u00020\u0003J\u0006\u0010\u0018\u001a\u00020\nJ\u0006\u0010\u0019\u001a\u00020\u001aJ\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotQueue;", "", "maxMemoryMB", "", "<init>", "(I)V", "queue", "Ljava/util/concurrent/LinkedBlockingQueue;", "Lorg/pcfx/adapter/android/screenshot/ScreenshotQueue$QueuedScreenshot;", "currentMemoryUsageBytes", "", "maxMemoryBytes", "lock", "enqueue", "", "bitmap", "Landroid/graphics/Bitmap;", "timestamp", "dequeue", "Lkotlin/Pair;", "dequeueAndDispose", "clear", "", "size", "currentMemoryUsage", "getMemoryPercentage", "", "formatBytes", "", "bytes", "QueuedScreenshot", "Companion", "Android-A1_debug"})
public final class ScreenshotQueue {
    private final int maxMemoryMB = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.LinkedBlockingQueue<org.pcfx.adapter.android.screenshot.ScreenshotQueue.QueuedScreenshot> queue = null;
    private long currentMemoryUsageBytes = 0L;
    private final long maxMemoryBytes = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object lock = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ScreenshotQueue";
    private static final int BYTES_PER_PIXEL = 4;
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.screenshot.ScreenshotQueue.Companion Companion = null;
    
    public ScreenshotQueue(int maxMemoryMB) {
        super();
    }
    
    public final boolean enqueue(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, long timestamp) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.Pair<android.graphics.Bitmap, java.lang.Long> dequeue() {
        return null;
    }
    
    private final kotlin.Pair<android.graphics.Bitmap, java.lang.Long> dequeueAndDispose() {
        return null;
    }
    
    public final void clear() {
    }
    
    public final int size() {
        return 0;
    }
    
    public final long currentMemoryUsage() {
        return 0L;
    }
    
    public final float getMemoryPercentage() {
        return 0.0F;
    }
    
    private final java.lang.String formatBytes(long bytes) {
        return null;
    }
    
    public ScreenshotQueue() {
        super();
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotQueue$Companion;", "", "<init>", "()V", "TAG", "", "BYTES_PER_PIXEL", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f\u00a8\u0006\u0019"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotQueue$QueuedScreenshot;", "", "bitmap", "Landroid/graphics/Bitmap;", "timestamp", "", "estimatedSizeBytes", "<init>", "(Landroid/graphics/Bitmap;JJ)V", "getBitmap", "()Landroid/graphics/Bitmap;", "getTimestamp", "()J", "getEstimatedSizeBytes", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Android-A1_debug"})
    static final class QueuedScreenshot {
        @org.jetbrains.annotations.NotNull()
        private final android.graphics.Bitmap bitmap = null;
        private final long timestamp = 0L;
        private final long estimatedSizeBytes = 0L;
        
        public QueuedScreenshot(@org.jetbrains.annotations.NotNull()
        android.graphics.Bitmap bitmap, long timestamp, long estimatedSizeBytes) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.Bitmap getBitmap() {
            return null;
        }
        
        public final long getTimestamp() {
            return 0L;
        }
        
        public final long getEstimatedSizeBytes() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.Bitmap component1() {
            return null;
        }
        
        public final long component2() {
            return 0L;
        }
        
        public final long component3() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final org.pcfx.adapter.android.screenshot.ScreenshotQueue.QueuedScreenshot copy(@org.jetbrains.annotations.NotNull()
        android.graphics.Bitmap bitmap, long timestamp, long estimatedSizeBytes) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}