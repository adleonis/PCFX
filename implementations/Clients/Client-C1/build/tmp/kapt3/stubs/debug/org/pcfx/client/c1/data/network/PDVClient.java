package org.pcfx.client.c1.data.network;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010$\n\u0002\b\b\b\u0007\u0018\u0000 $2\u00020\u0001:\u0001$B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nJ0\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0006H\u0002J0\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\f2\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0011J0\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\f2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0017\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0019J0\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00140\f2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0017\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001b\u0010\u0019J(\u0010\u001c\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u001d0\fH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u001a\u0010 \u001a\u00020\n2\b\b\u0002\u0010!\u001a\u00020\u00062\b\b\u0002\u0010\"\u001a\u00020\bJ\u0016\u0010#\u001a\u00020\n2\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006%"}, d2 = {"Lorg/pcfx/client/c1/data/network/PDVClient;", "", "()V", "httpClient", "Lio/ktor/client/HttpClient;", "pdvHost", "", "pdvPort", "", "close", "", "getAtoms", "Lkotlin/Result;", "Lorg/pcfx/client/c1/data/models/AtomResponse;", "since", "limit", "getAtoms-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBaseUrl", "getEvents", "Lorg/pcfx/client/c1/data/models/EventResponse;", "getEvents-0E7RQCE", "getRecentAtoms", "offset", "getRecentAtoms-0E7RQCE", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentEvents", "getRecentEvents-0E7RQCE", "getStats", "", "getStats-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initialize", "host", "port", "setServer", "Companion", "ClientC1_debug"})
public final class PDVClient {
    private io.ktor.client.HttpClient httpClient;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String pdvHost = "localhost";
    private int pdvPort = 7777;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PDVClient";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.client.c1.data.network.PDVClient.Companion Companion = null;
    
    @javax.inject.Inject()
    public PDVClient() {
        super();
    }
    
    public final void initialize(@org.jetbrains.annotations.NotNull()
    java.lang.String host, int port) {
    }
    
    public final void setServer(@org.jetbrains.annotations.NotNull()
    java.lang.String host, int port) {
    }
    
    private final java.lang.String getBaseUrl() {
        return null;
    }
    
    public final void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/pcfx/client/c1/data/network/PDVClient$Companion;", "", "()V", "TAG", "", "ClientC1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}