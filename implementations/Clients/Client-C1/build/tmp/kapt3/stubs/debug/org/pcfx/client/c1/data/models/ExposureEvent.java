package org.pcfx.client.c1.data.models;

@kotlinx.serialization.Serializable()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u001f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001By\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\u0016\b\u0002\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0012\u00a2\u0006\u0002\u0010\u0013J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\u0017\u0010\'\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0012H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\t\u0010-\u001a\u00020\u000bH\u00c6\u0003J\t\u0010.\u001a\u00020\rH\u00c6\u0003J\t\u0010/\u001a\u00020\u000fH\u00c6\u0003J\u008b\u0001\u00100\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00032\u0016\b\u0002\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0012H\u00c6\u0001J\u0013\u00101\u001a\u0002022\b\u00103\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00104\u001a\u000205H\u00d6\u0001J\t\u00106\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u001f\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0015R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0015R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0015R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0015\u00a8\u00067"}, d2 = {"Lorg/pcfx/client/c1/data/models/ExposureEvent;", "", "id", "", "ts", "device", "adapter_id", "schema", "capabilities_used", "", "source", "Lorg/pcfx/client/c1/data/models/EventSource;", "content", "Lorg/pcfx/client/c1/data/models/EventContent;", "privacy", "Lorg/pcfx/client/c1/data/models/EventPrivacy;", "signature", "extensions", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lorg/pcfx/client/c1/data/models/EventSource;Lorg/pcfx/client/c1/data/models/EventContent;Lorg/pcfx/client/c1/data/models/EventPrivacy;Ljava/lang/String;Ljava/util/Map;)V", "getAdapter_id", "()Ljava/lang/String;", "getCapabilities_used", "()Ljava/util/List;", "getContent", "()Lorg/pcfx/client/c1/data/models/EventContent;", "getDevice", "getExtensions", "()Ljava/util/Map;", "getId", "getPrivacy", "()Lorg/pcfx/client/c1/data/models/EventPrivacy;", "getSchema", "getSignature", "getSource", "()Lorg/pcfx/client/c1/data/models/EventSource;", "getTs", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "ClientC1_debug"})
public final class ExposureEvent {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String ts = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String device = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String adapter_id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String schema = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> capabilities_used = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.models.EventSource source = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.models.EventContent content = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.models.EventPrivacy privacy = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String signature = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.Map<java.lang.String, java.lang.Object> extensions = null;
    
    public ExposureEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String ts, @org.jetbrains.annotations.NotNull()
    java.lang.String device, @org.jetbrains.annotations.NotNull()
    java.lang.String adapter_id, @org.jetbrains.annotations.NotNull()
    java.lang.String schema, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> capabilities_used, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.models.EventSource source, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.models.EventContent content, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.models.EventPrivacy privacy, @org.jetbrains.annotations.NotNull()
    java.lang.String signature, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.lang.Object> extensions) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDevice() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAdapter_id() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSchema() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getCapabilities_used() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.EventSource getSource() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.EventContent getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.EventPrivacy getPrivacy() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSignature() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.lang.Object> getExtensions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.lang.Object> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.EventSource component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.EventContent component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.EventPrivacy component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.data.models.ExposureEvent copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String ts, @org.jetbrains.annotations.NotNull()
    java.lang.String device, @org.jetbrains.annotations.NotNull()
    java.lang.String adapter_id, @org.jetbrains.annotations.NotNull()
    java.lang.String schema, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> capabilities_used, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.models.EventSource source, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.models.EventContent content, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.models.EventPrivacy privacy, @org.jetbrains.annotations.NotNull()
    java.lang.String signature, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.lang.Object> extensions) {
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