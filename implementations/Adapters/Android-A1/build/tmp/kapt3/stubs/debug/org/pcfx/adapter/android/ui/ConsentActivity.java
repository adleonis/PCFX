package org.pcfx.adapter.android.ui;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J\b\u0010\u0010\u001a\u00020\rH\u0014J\b\u0010\u0011\u001a\u00020\rH\u0002J\b\u0010\u0012\u001a\u00020\rH\u0002J\b\u0010\u0013\u001a\u00020\rH\u0002J\b\u0010\u0014\u001a\u00020\rH\u0002J\b\u0010\u0015\u001a\u00020\rH\u0002J\"\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\rH\u0002J\b\u0010\u001d\u001a\u00020\rH\u0002J\b\u0010\u001e\u001a\u00020\rH\u0002J\b\u0010\u001f\u001a\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lorg/pcfx/adapter/android/ui/ConsentActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "consentManager", "Lorg/pcfx/adapter/android/consent/ConsentManager;", "keyManager", "Lorg/pcfx/adapter/android/security/KeyManager;", "recordingHelper", "Lorg/pcfx/adapter/android/recording/VideoRecordingHelper;", "isRecording", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "setupUI", "checkAccessibilityServiceStatus", "updateButtonStates", "toggleVideoRecording", "updateVideoRecordingButtonText", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "acceptConsent", "declineConsent", "showConsentStatus", "regenerateKeyPair", "Android-A1_debug"})
public final class ConsentActivity extends androidx.appcompat.app.AppCompatActivity {
    private org.pcfx.adapter.android.consent.ConsentManager consentManager;
    private org.pcfx.adapter.android.security.KeyManager keyManager;
    private org.pcfx.adapter.android.recording.VideoRecordingHelper recordingHelper;
    private boolean isRecording = false;
    
    public ConsentActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void setupUI() {
    }
    
    private final void checkAccessibilityServiceStatus() {
    }
    
    private final void updateButtonStates() {
    }
    
    private final void toggleVideoRecording() {
    }
    
    private final void updateVideoRecordingButtonText() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void acceptConsent() {
    }
    
    private final void declineConsent() {
    }
    
    private final void showConsentStatus() {
    }
    
    private final void regenerateKeyPair() {
    }
}