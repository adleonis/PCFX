package org.pcfx.adapter.android.screenshot;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005J\b\u0010\u000f\u001a\u0004\u0018\u00010\u0005J\u0006\u0010\u0010\u001a\u00020\rR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotDeduplicator;", "", "<init>", "()V", "lastScreenshotHash", "", "lock", "computeHash", "bitmap", "Landroid/graphics/Bitmap;", "isDuplicate", "", "updateLastHash", "", "hash", "getLastHash", "reset", "Companion", "Android-A1_debug"})
public final class ScreenshotDeduplicator {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastScreenshotHash;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object lock = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ScreenshotDeduplicator";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.screenshot.ScreenshotDeduplicator.Companion Companion = null;
    
    public ScreenshotDeduplicator() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String computeHash(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    public final boolean isDuplicate(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return false;
    }
    
    public final void updateLastHash(@org.jetbrains.annotations.NotNull()
    java.lang.String hash) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastHash() {
        return null;
    }
    
    public final void reset() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/ScreenshotDeduplicator$Companion;", "", "<init>", "()V", "TAG", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}