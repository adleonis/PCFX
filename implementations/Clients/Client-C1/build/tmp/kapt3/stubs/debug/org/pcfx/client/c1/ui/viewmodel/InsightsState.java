package org.pcfx.client.c1.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0018\b\u0087\b\u0018\u00002\u00020\u0001Bg\u0012\u001a\b\u0002\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u0003\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003\u0012\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\u0004\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u000fJ\u001b\u0010\u001a\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u0003H\u00c6\u0003J\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003H\u00c6\u0003J\u0015\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\u0004H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\rH\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003Jk\u0010 \u001a\u00020\u00002\u001a\b\u0002\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00032\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\u00042\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010!\u001a\u00020\r2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\tH\u00d6\u0001J\t\u0010$\u001a\u00020\u0005H\u00d6\u0001R#\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0016R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011\u00a8\u0006%"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/InsightsState;", "", "atoms", "", "", "", "topEntities", "Lorg/pcfx/client/c1/ui/viewmodel/EntityInfo;", "sentimentDistribution", "", "averageConfidence", "", "isLoading", "", "error", "(Ljava/util/List;Ljava/util/List;Ljava/util/Map;DZLjava/lang/String;)V", "getAtoms", "()Ljava/util/List;", "getAverageConfidence", "()D", "getError", "()Ljava/lang/String;", "()Z", "getSentimentDistribution", "()Ljava/util/Map;", "getTopEntities", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "ClientC1_debug"})
public final class InsightsState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> atoms = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<org.pcfx.client.c1.ui.viewmodel.EntityInfo> topEntities = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Integer> sentimentDistribution = null;
    private final double averageConfidence = 0.0;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public InsightsState(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.util.Map<java.lang.String, ? extends java.lang.Object>> atoms, @org.jetbrains.annotations.NotNull()
    java.util.List<org.pcfx.client.c1.ui.viewmodel.EntityInfo> topEntities, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> sentimentDistribution, double averageConfidence, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> getAtoms() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.pcfx.client.c1.ui.viewmodel.EntityInfo> getTopEntities() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> getSentimentDistribution() {
        return null;
    }
    
    public final double getAverageConfidence() {
        return 0.0;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public InsightsState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.pcfx.client.c1.ui.viewmodel.EntityInfo> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> component3() {
        return null;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.ui.viewmodel.InsightsState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.util.Map<java.lang.String, ? extends java.lang.Object>> atoms, @org.jetbrains.annotations.NotNull()
    java.util.List<org.pcfx.client.c1.ui.viewmodel.EntityInfo> topEntities, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> sentimentDistribution, double averageConfidence, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
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