package org.pcfx.client.c1.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010$\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b&\b\u0087\b\u0018\u00002\u00020\u0001B\u009d\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\u001a\b\u0002\u0010\n\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\f0\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u0015J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\'\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0012H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0007H\u00c6\u0003J\t\u0010+\u001a\u00020\u0005H\u00c6\u0003J\t\u0010,\u001a\u00020\u0007H\u00c6\u0003J\t\u0010-\u001a\u00020\u0005H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\u001b\u0010/\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\f0\u000bH\u00c6\u0003J\t\u00100\u001a\u00020\u0007H\u00c6\u0003J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\u00a1\u0001\u00103\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00032\u001a\b\u0002\u0010\n\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\f0\u000b2\b\b\u0002\u0010\r\u001a\u00020\u00072\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u0007H\u00c6\u0001J\u0013\u00104\u001a\u00020\u00072\b\u00105\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00106\u001a\u00020\u0005H\u00d6\u0001J\t\u00107\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0014\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u001aR\u0011\u0010\r\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010 R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0017R#\u0010\n\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0017\u00a8\u00068"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/SettingsState;", "", "pdvHost", "", "pdvPort", "", "autoRefreshEnabled", "", "refreshIntervalMs", "searchQuery", "searchResults", "", "", "isSearching", "appId", "appVersion", "successMessage", "healthCheckStatus", "Lorg/pcfx/client/c1/ui/viewmodel/HealthCheckStatus;", "healthCheckMessage", "isCheckingHealth", "(Ljava/lang/String;IZILjava/lang/String;Ljava/util/List;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/pcfx/client/c1/ui/viewmodel/HealthCheckStatus;Ljava/lang/String;Z)V", "getAppId", "()Ljava/lang/String;", "getAppVersion", "getAutoRefreshEnabled", "()Z", "getHealthCheckMessage", "getHealthCheckStatus", "()Lorg/pcfx/client/c1/ui/viewmodel/HealthCheckStatus;", "getPdvHost", "getPdvPort", "()I", "getRefreshIntervalMs", "getSearchQuery", "getSearchResults", "()Ljava/util/List;", "getSuccessMessage", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "ClientC1_debug"})
public final class SettingsState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String pdvHost = null;
    private final int pdvPort = 0;
    private final boolean autoRefreshEnabled = false;
    private final int refreshIntervalMs = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> searchResults = null;
    private final boolean isSearching = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String appId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String appVersion = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String successMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.ui.viewmodel.HealthCheckStatus healthCheckStatus = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String healthCheckMessage = null;
    private final boolean isCheckingHealth = false;
    
    public SettingsState(@org.jetbrains.annotations.NotNull()
    java.lang.String pdvHost, int pdvPort, boolean autoRefreshEnabled, int refreshIntervalMs, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.util.Map<java.lang.String, ? extends java.lang.Object>> searchResults, boolean isSearching, @org.jetbrains.annotations.NotNull()
    java.lang.String appId, @org.jetbrains.annotations.NotNull()
    java.lang.String appVersion, @org.jetbrains.annotations.Nullable()
    java.lang.String successMessage, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.ui.viewmodel.HealthCheckStatus healthCheckStatus, @org.jetbrains.annotations.Nullable()
    java.lang.String healthCheckMessage, boolean isCheckingHealth) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPdvHost() {
        return null;
    }
    
    public final int getPdvPort() {
        return 0;
    }
    
    public final boolean getAutoRefreshEnabled() {
        return false;
    }
    
    public final int getRefreshIntervalMs() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> getSearchResults() {
        return null;
    }
    
    public final boolean isSearching() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAppId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAppVersion() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSuccessMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.ui.viewmodel.HealthCheckStatus getHealthCheckStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getHealthCheckMessage() {
        return null;
    }
    
    public final boolean isCheckingHealth() {
        return false;
    }
    
    public SettingsState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.ui.viewmodel.HealthCheckStatus component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.client.c1.ui.viewmodel.SettingsState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String pdvHost, int pdvPort, boolean autoRefreshEnabled, int refreshIntervalMs, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.util.Map<java.lang.String, ? extends java.lang.Object>> searchResults, boolean isSearching, @org.jetbrains.annotations.NotNull()
    java.lang.String appId, @org.jetbrains.annotations.NotNull()
    java.lang.String appVersion, @org.jetbrains.annotations.Nullable()
    java.lang.String successMessage, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.ui.viewmodel.HealthCheckStatus healthCheckStatus, @org.jetbrains.annotations.Nullable()
    java.lang.String healthCheckMessage, boolean isCheckingHealth) {
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