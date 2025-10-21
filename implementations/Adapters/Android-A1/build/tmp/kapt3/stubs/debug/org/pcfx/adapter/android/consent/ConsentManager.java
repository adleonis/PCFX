package org.pcfx.adapter.android.consent;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\b\u0010\u000e\u001a\u0004\u0018\u00010\rJ\u0006\u0010\u000f\u001a\u00020\u000bJ\u0006\u0010\u0010\u001a\u00020\u000bJ\u0006\u0010\u0011\u001a\u00020\u0012J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014J\u000e\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u001a\u001a\u00020\u0018R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lorg/pcfx/adapter/android/consent/ConsentManager;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "sharedPrefs", "Landroid/content/SharedPreferences;", "gson", "Lcom/google/gson/Gson;", "saveConsent", "", "manifest", "Lorg/pcfx/adapter/android/model/ConsentManifest;", "getActiveConsent", "revokeConsent", "clearConsent", "isConsentActive", "", "getAllGrants", "", "Lorg/pcfx/adapter/android/model/ConsentManifest$Grant;", "hasCapability", "capability", "", "getGrantPurpose", "formatConsentForDisplay", "Companion", "Android-A1_debug"})
public final class ConsentManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences sharedPrefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_CONSENT_JSON = "consent_manifest_json";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_IS_ENABLED = "consent_enabled";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.adapter.android.consent.ConsentManager.Companion Companion = null;
    
    public ConsentManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void saveConsent(@org.jetbrains.annotations.NotNull()
    org.pcfx.adapter.android.model.ConsentManifest manifest) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.pcfx.adapter.android.model.ConsentManifest getActiveConsent() {
        return null;
    }
    
    public final void revokeConsent() {
    }
    
    public final void clearConsent() {
    }
    
    public final boolean isConsentActive() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.pcfx.adapter.android.model.ConsentManifest.Grant> getAllGrants() {
        return null;
    }
    
    public final boolean hasCapability(@org.jetbrains.annotations.NotNull()
    java.lang.String capability) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getGrantPurpose(@org.jetbrains.annotations.NotNull()
    java.lang.String capability) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatConsentForDisplay() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lorg/pcfx/adapter/android/consent/ConsentManager$Companion;", "", "<init>", "()V", "PREFS_CONSENT_JSON", "", "PREFS_IS_ENABLED", "Android-A1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}