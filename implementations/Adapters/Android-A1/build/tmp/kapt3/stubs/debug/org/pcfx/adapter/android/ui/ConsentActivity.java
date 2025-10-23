package org.pcfx.adapter.android.ui;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u0016H\u0014J\b\u0010\u001a\u001a\u00020\u0016H\u0002J\b\u0010\u001b\u001a\u00020\u0016H\u0002J\b\u0010\u001c\u001a\u00020\u0016H\u0002J\b\u0010\u001d\u001a\u00020\u0016H\u0002J\u0012\u0010\u001e\u001a\u00020\u00162\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0002J\b\u0010!\u001a\u00020\u0016H\u0002J\b\u0010\"\u001a\u00020\u0016H\u0002J\b\u0010#\u001a\u00020\u0016H\u0002J\b\u0010$\u001a\u00020\u0016H\u0002J\"\u0010%\u001a\u00020\u00162\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020'2\b\u0010)\u001a\u0004\u0018\u00010*H\u0014J\b\u0010+\u001a\u00020\u0016H\u0002J\b\u0010,\u001a\u00020\u0016H\u0002J\b\u0010-\u001a\u00020\u0016H\u0002J\b\u0010.\u001a\u00020\u0016H\u0002J\b\u0010/\u001a\u00020\u0016H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lorg/pcfx/adapter/android/ui/ConsentActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "consentManager", "Lorg/pcfx/adapter/android/consent/ConsentManager;", "keyManager", "Lorg/pcfx/adapter/android/security/KeyManager;", "recordingHelper", "Lorg/pcfx/adapter/android/recording/VideoRecordingHelper;", "screenshotCaptureManager", "Lorg/pcfx/adapter/android/screenshot/ScreenshotCaptureManager;", "pdvClient", "Lorg/pcfx/adapter/android/network/PDVClient;", "pdvStatusIcon", "Landroid/widget/ImageView;", "pdvStatusText", "Landroid/widget/TextView;", "isRecording", "", "isScreenshotCapturing", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "setupUI", "setupScreenshotCaptureUI", "startScreenshotCapture", "stopScreenshotCapture", "updateScreenshotToggle", "toggle", "Landroid/widget/Switch;", "checkAccessibilityServiceStatus", "updateButtonStates", "toggleVideoRecording", "updateVideoRecordingButtonText", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "acceptConsent", "declineConsent", "showConsentStatus", "regenerateKeyPair", "checkPdvConnectivity", "Android-A1_debug"})
public final class ConsentActivity extends androidx.appcompat.app.AppCompatActivity {
    private org.pcfx.adapter.android.consent.ConsentManager consentManager;
    private org.pcfx.adapter.android.security.KeyManager keyManager;
    private org.pcfx.adapter.android.recording.VideoRecordingHelper recordingHelper;
    private org.pcfx.adapter.android.screenshot.ScreenshotCaptureManager screenshotCaptureManager;
    private org.pcfx.adapter.android.network.PDVClient pdvClient;
    private android.widget.ImageView pdvStatusIcon;
    private android.widget.TextView pdvStatusText;
    private boolean isRecording = false;
    private boolean isScreenshotCapturing = false;
    
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
    
    private final void setupScreenshotCaptureUI() {
    }
    
    private final void startScreenshotCapture() {
    }
    
    private final void stopScreenshotCapture() {
    }
    
    private final void updateScreenshotToggle(android.widget.Switch toggle) {
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
    
    private final void checkPdvConnectivity() {
    }
}