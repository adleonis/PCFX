package org.pcfx.adapter.android.network;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\u0018\u0000 /2\u00020\u0001:\u0002/0B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000bJ<\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000b2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0017H\u0086@\u00a2\u0006\u0002\u0010\u0018JL\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000b2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00172\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010\u001dJ.\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000b2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0017H\u0002J.\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010$J>\u0010%\u001a\u00020\u00102\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010&J\u0006\u0010'\u001a\u00020(J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u001bH\u0002J\b\u0010,\u001a\u00020\u000bH\u0002J\u0010\u0010-\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020\"H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2 = {"Lorg/pcfx/adapter/android/network/PDVClient;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "client", "Lokhttp3/OkHttpClient;", "sharedPrefs", "Landroid/content/SharedPreferences;", "getPDVUrl", "", "setPDVUrl", "", "url", "postEvent", "Lorg/pcfx/adapter/android/network/PDVClient$Result;", "event", "Lorg/pcfx/adapter/android/model/ExposureEvent;", "eventJson", "adapterId", "consentId", "capabilitiesUsed", "", "(Lorg/pcfx/adapter/android/model/ExposureEvent;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postEventWithRetry", "retryCount", "", "maxRetries", "(Lorg/pcfx/adapter/android/model/ExposureEvent;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postEventRequest", "Lokhttp3/Response;", "postBlob", "blobData", "", "contentType", "([BLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postBlobWithRetry", "([BLjava/lang/String;Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isOnline", "", "calculateBackoff", "", "attempt", "generateNonce", "calculateSha256", "data", "Companion", "Result", "Android-A1_debug"})
public final class PDVClient {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences sharedPrefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_PDV_URL = "pdv_url";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_DEFAULT_URL = "http://127.0.0.1:7777";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.network.PDVClient.Companion Companion = null;
    
    public PDVClient(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPDVUrl() {
        return null;
    }
    
    public final void setPDVUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object postEvent(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.model.ExposureEvent event, @org.jetbrains.annotations.NotNull()
    java.lang.String eventJson, @org.jetbrains.annotations.NotNull()
    java.lang.String adapterId, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> capabilitiesUsed, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super org.pcfx.adapter.android.network.PDVClient.Result> $completion) {
        return null;
    }
    
    private final java.lang.Object postEventWithRetry(org.pcfx.adapter.android.model.ExposureEvent event, java.lang.String eventJson, java.lang.String adapterId, java.lang.String consentId, java.util.List<java.lang.String> capabilitiesUsed, int retryCount, int maxRetries, kotlin.coroutines.Continuation<? super org.pcfx.adapter.android.network.PDVClient.Result> $completion) {
        return null;
    }
    
    private final okhttp3.Response postEventRequest(java.lang.String eventJson, java.lang.String adapterId, java.lang.String consentId, java.util.List<java.lang.String> capabilitiesUsed) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object postBlob(@org.jetbrains.annotations.NotNull()
    byte[] blobData, @org.jetbrains.annotations.NotNull()
    java.lang.String contentType, @org.jetbrains.annotations.NotNull()
    java.lang.String adapterId, @org.jetbrains.annotations.NotNull()
    java.lang.String consentId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super org.pcfx.adapter.android.network.PDVClient.Result> $completion) {
        return null;
    }
    
    private final java.lang.Object postBlobWithRetry(byte[] blobData, java.lang.String contentType, java.lang.String adapterId, java.lang.String consentId, int retryCount, int maxRetries, kotlin.coroutines.Continuation<? super org.pcfx.adapter.android.network.PDVClient.Result> $completion) {
        return null;
    }
    
    public final boolean isOnline() {
        return false;
    }
    
    private final long calculateBackoff(int attempt) {
        return 0L;
    }
    
    private final java.lang.String generateNonce() {
        return null;
    }
    
    private final java.lang.String calculateSha256(byte[] data) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lorg/pcfx/adapter/android/network/PDVClient$Companion;", "", "<init>", "()V", "PREFS_PDV_URL", "", "PREFS_DEFAULT_URL", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007\u00a8\u0006\b"}, d2 = {"Lorg/pcfx/adapter/android/network/PDVClient$Result;", "", "<init>", "()V", "Success", "Failure", "Lorg/pcfx/adapter/android/network/PDVClient$Result$Failure;", "Lorg/pcfx/adapter/android/network/PDVClient$Result$Success;", "Android-A1_debug"})
    public static abstract class Result {
        
        private Result() {
            super();
        }
        
        @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\n\u00a8\u0006\u0014"}, d2 = {"Lorg/pcfx/adapter/android/network/PDVClient$Result$Failure;", "Lorg/pcfx/adapter/android/network/PDVClient$Result;", "message", "", "isRetryable", "", "<init>", "(Ljava/lang/String;Z)V", "getMessage", "()Ljava/lang/String;", "()Z", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "Android-A1_debug"})
        public static final class Failure extends org.pcfx.adapter.android.network.PDVClient.Result {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;
            private final boolean isRetryable = false;
            
            public Failure(@org.jetbrains.annotations.NotNull()
            java.lang.String message, boolean isRetryable) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
            
            public final boolean isRetryable() {
                return false;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            public final boolean component2() {
                return false;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final org.pcfx.adapter.android.network.PDVClient.Result.Failure copy(@org.jetbrains.annotations.NotNull()
            java.lang.String message, boolean isRetryable) {
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
        
        @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0011"}, d2 = {"Lorg/pcfx/adapter/android/network/PDVClient$Result$Success;", "Lorg/pcfx/adapter/android/network/PDVClient$Result;", "data", "", "<init>", "(Ljava/lang/String;)V", "getData", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "Android-A1_debug"})
        public static final class Success extends org.pcfx.adapter.android.network.PDVClient.Result {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String data = null;
            
            public Success(@org.jetbrains.annotations.NotNull()
            java.lang.String data) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getData() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final org.pcfx.adapter.android.network.PDVClient.Result.Success copy(@org.jetbrains.annotations.NotNull()
            java.lang.String data) {
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
}