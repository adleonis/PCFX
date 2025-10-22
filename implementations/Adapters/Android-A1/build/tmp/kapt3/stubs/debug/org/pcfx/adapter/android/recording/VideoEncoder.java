package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\u0014\u001a\u00020\u000fJ\b\u0010\u0015\u001a\u0004\u0018\u00010\rJ\u0010\u0010\u0016\u001a\u00020\u00122\b\b\u0002\u0010\u0017\u001a\u00020\u000fJ\u0006\u0010\u0018\u001a\u00020\u0012J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoEncoder;", "", "outputFile", "Ljava/io/File;", "config", "Lorg/pcfx/adapter/android/recording/RecordingConfig;", "<init>", "(Ljava/io/File;Lorg/pcfx/adapter/android/recording/RecordingConfig;)V", "mediaCodec", "Landroid/media/MediaCodec;", "mediaMuxer", "Landroid/media/MediaMuxer;", "encoderSurface", "Landroid/view/Surface;", "isInitialized", "", "isFormatSet", "videoTrackIndex", "", "frameCount", "initialize", "getInputSurface", "drainEncoderForChunk", "endOfChunk", "finalizeChunk", "stop", "", "getFrameCount", "Companion", "Android-A1_debug"})
public final class VideoEncoder {
    @org.jetbrains.annotations.NotNull()
    private final java.io.File outputFile = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.adapter.android.recording.RecordingConfig config = null;
    private android.media.MediaCodec mediaCodec;
    private android.media.MediaMuxer mediaMuxer;
    private android.view.Surface encoderSurface;
    private boolean isInitialized = false;
    private boolean isFormatSet = false;
    private int videoTrackIndex = -1;
    private int frameCount = 0;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "VideoEncoder";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.recording.VideoEncoder.Companion Companion = null;
    
    public VideoEncoder(@org.jetbrains.annotations.NotNull()
    java.io.File outputFile, @org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.recording.RecordingConfig config) {
        super();
    }
    
    public final boolean initialize() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.Surface getInputSurface() {
        return null;
    }
    
    public final int drainEncoderForChunk(boolean endOfChunk) {
        return 0;
    }
    
    public final int finalizeChunk() {
        return 0;
    }
    
    public final void stop() {
    }
    
    public final int getFrameCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoEncoder$Companion;", "", "<init>", "()V", "TAG", "", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}