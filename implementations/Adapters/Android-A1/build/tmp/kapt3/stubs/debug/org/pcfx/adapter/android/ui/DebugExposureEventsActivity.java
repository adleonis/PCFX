package org.pcfx.adapter.android.ui;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014J\b\u0010\u000e\u001a\u00020\u000bH\u0014J\b\u0010\u000f\u001a\u00020\u000bH\u0002J \u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0019H\u0002J\b\u0010\u001c\u001a\u00020\u000bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lorg/pcfx/adapter/android/ui/DebugExposureEventsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "gson", "Lcom/google/gson/Gson;", "eventsContainer", "Landroid/widget/LinearLayout;", "loadingText", "Landroid/widget/TextView;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "loadEvents", "createEventView", "Landroid/view/View;", "index", "", "event", "Lorg/pcfx/adapter/android/model/ExposureEvent;", "isPosted", "", "buildEventDetails", "", "formatTimestamp", "isoTimestamp", "createManualTestEvent", "Android-A1_debug"})
public final class DebugExposureEventsActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    private android.widget.LinearLayout eventsContainer;
    private android.widget.TextView loadingText;
    
    public DebugExposureEventsActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void loadEvents() {
    }
    
    private final android.view.View createEventView(int index, org.pcfx.adapter.android.model.ExposureEvent event, boolean isPosted) {
        return null;
    }
    
    private final java.lang.String buildEventDetails(org.pcfx.adapter.android.model.ExposureEvent event) {
        return null;
    }
    
    private final java.lang.String formatTimestamp(java.lang.String isoTimestamp) {
        return null;
    }
    
    private final void createManualTestEvent() {
    }
}