package org.pcfx.adapter.android.security;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u00162\u00020\u0001:\u0002\u0016\u0017B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\rJ\b\u0010\u000e\u001a\u00020\u000bH\u0002J\n\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\n\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J\b\u0010\u0013\u001a\u00020\rH\u0002J\u0006\u0010\u0014\u001a\u00020\u0015R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lorg/pcfx/adapter/android/security/KeyManager;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "sharedPrefs", "Landroid/content/SharedPreferences;", "keyStore", "Ljava/security/KeyStore;", "getOrGenerateKeyPair", "Lorg/pcfx/adapter/android/security/KeyManager$KeyPair;", "regenerateKeyPair", "", "generateNewKeyPair", "getPrivateKey", "Ljava/security/PrivateKey;", "getPublicKey", "Ljava/security/PublicKey;", "deleteCurrentKeyPair", "getPublicKeyBase64", "", "Companion", "KeyPair", "Android-A1_debug"})
public final class KeyManager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEYSTORE_ALIAS = "pcfx_ed25519_key";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "pcfx_keys";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_PUBLIC_KEY = "public_key_base64";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEYSTORE_PROVIDER = "AndroidKeyStore";
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences sharedPrefs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.security.KeyStore keyStore = null;
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.security.KeyManager.Companion Companion = null;
    
    public KeyManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.pcfx.adapter.android.security.KeyManager.KeyPair getOrGenerateKeyPair() {
        return null;
    }
    
    public final void regenerateKeyPair() {
    }
    
    private final org.pcfx.adapter.android.security.KeyManager.KeyPair generateNewKeyPair() {
        return null;
    }
    
    private final java.security.PrivateKey getPrivateKey() {
        return null;
    }
    
    private final java.security.PublicKey getPublicKey() {
        return null;
    }
    
    private final void deleteCurrentKeyPair() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPublicKeyBase64() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lorg/pcfx/adapter/android/security/KeyManager$Companion;", "", "<init>", "()V", "KEYSTORE_ALIAS", "", "PREFS_NAME", "PREFS_PUBLIC_KEY", "KEYSTORE_PROVIDER", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lorg/pcfx/adapter/android/security/KeyManager$KeyPair;", "", "privateKey", "Ljava/security/PrivateKey;", "publicKey", "Ljava/security/PublicKey;", "<init>", "(Ljava/security/PrivateKey;Ljava/security/PublicKey;)V", "getPrivateKey", "()Ljava/security/PrivateKey;", "getPublicKey", "()Ljava/security/PublicKey;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Android-A1_debug"})
    public static final class KeyPair {
        @org.jetbrains.annotations.NotNull()
        private final java.security.PrivateKey privateKey = null;
        @org.jetbrains.annotations.NotNull()
        private final java.security.PublicKey publicKey = null;
        
        public KeyPair(@org.jetbrains.annotations.NotNull()
        java.security.PrivateKey privateKey, @org.jetbrains.annotations.NotNull()
        java.security.PublicKey publicKey) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.security.PrivateKey getPrivateKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.security.PublicKey getPublicKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.security.PrivateKey component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.security.PublicKey component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final org.pcfx.adapter.android.security.KeyManager.KeyPair copy(@org.jetbrains.annotations.NotNull()
        java.security.PrivateKey privateKey, @org.jetbrains.annotations.NotNull()
        java.security.PublicKey publicKey) {
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