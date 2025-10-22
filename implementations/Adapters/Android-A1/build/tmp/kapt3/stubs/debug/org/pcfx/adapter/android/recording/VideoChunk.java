package org.pcfx.adapter.android.recording;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\b\u0018\u00002\u00020\u0001BG\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u00a2\u0006\u0004\b\r\u0010\u000eJ\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\u00010\"J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0005H\u00c6\u0003J\t\u0010&\u001a\u00020\u0007H\u00c6\u0003J\t\u0010'\u001a\u00020\u0007H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u000bH\u00c6\u0003J\t\u0010*\u001a\u00020\u000bH\u00c6\u0003JO\u0010+\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010,\u001a\u00020\u000b2\b\u0010-\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010.\u001a\u00020\u0003H\u00d6\u0001J\t\u0010/\u001a\u00020#H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001a\u0010\b\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0014\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0010\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\u001b\"\u0004\b\u001e\u0010\u001dR\u0011\u0010\u001f\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b \u0010\u0014\u00a8\u00060"}, d2 = {"Lorg/pcfx/adapter/android/recording/VideoChunk;", "", "sequenceNumber", "", "file", "Ljava/io/File;", "startTimestamp", "", "endTimestamp", "frameCount", "isFinalized", "", "isUploaded", "<init>", "(ILjava/io/File;JJIZZ)V", "getSequenceNumber", "()I", "getFile", "()Ljava/io/File;", "getStartTimestamp", "()J", "getEndTimestamp", "setEndTimestamp", "(J)V", "getFrameCount", "setFrameCount", "(I)V", "()Z", "setFinalized", "(Z)V", "setUploaded", "durationMs", "getDurationMs", "toMetadata", "", "", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "Android-A1_debug"})
public final class VideoChunk {
    private final int sequenceNumber = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.io.File file = null;
    private final long startTimestamp = 0L;
    private long endTimestamp;
    private int frameCount;
    private boolean isFinalized;
    private boolean isUploaded;
    
    public VideoChunk(int sequenceNumber, @org.jetbrains.annotations.NotNull()
    java.io.File file, long startTimestamp, long endTimestamp, int frameCount, boolean isFinalized, boolean isUploaded) {
        super();
    }
    
    public final int getSequenceNumber() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File getFile() {
        return null;
    }
    
    public final long getStartTimestamp() {
        return 0L;
    }
    
    public final long getEndTimestamp() {
        return 0L;
    }
    
    public final void setEndTimestamp(long p0) {
    }
    
    public final int getFrameCount() {
        return 0;
    }
    
    public final void setFrameCount(int p0) {
    }
    
    public final boolean isFinalized() {
        return false;
    }
    
    public final void setFinalized(boolean p0) {
    }
    
    public final boolean isUploaded() {
        return false;
    }
    
    public final void setUploaded(boolean p0) {
    }
    
    public final long getDurationMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Object> toMetadata() {
        return null;
    }
    
    public final int component1() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.recording.VideoChunk copy(int sequenceNumber, @org.jetbrains.annotations.NotNull()
    java.io.File file, long startTimestamp, long endTimestamp, int frameCount, boolean isFinalized, boolean isUploaded) {
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