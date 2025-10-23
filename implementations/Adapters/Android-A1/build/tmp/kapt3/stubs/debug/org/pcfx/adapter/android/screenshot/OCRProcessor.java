package org.pcfx.adapter.android.screenshot;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\u0007J\u0018\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0002J\u0006\u0010\u0015\u001a\u00020\nR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/OCRProcessor;", "", "<init>", "()V", "textRecognizer", "Lcom/google/mlkit/vision/text/TextRecognizer;", "isProcessing", "", "lock", "initialize", "", "isInitialized", "processScreenshot", "Lorg/pcfx/adapter/android/screenshot/OCRResult;", "bitmap", "Landroid/graphics/Bitmap;", "timestamp", "", "detectLanguage", "", "text", "release", "Companion", "Android-A1_debug"})
public final class OCRProcessor {
    @org.jetbrains.annotations.Nullable()
    private com.google.mlkit.vision.text.TextRecognizer textRecognizer;
    private boolean isProcessing = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object lock = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "OCRProcessor";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.screenshot.OCRProcessor.Companion Companion = null;
    
    public OCRProcessor() {
        super();
    }
    
    public final void initialize() {
    }
    
    public final boolean isInitialized() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.pcfx.adapter.android.screenshot.OCRResult processScreenshot(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, long timestamp) {
        return null;
    }
    
    private final java.lang.String detectLanguage(java.lang.String text) {
        return null;
    }
    
    public final void release() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/screenshot/OCRProcessor$Companion;", "", "<init>", "()V", "TAG", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}