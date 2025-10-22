package org.pcfx.client.c1.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0012\u001a\u00020\u0013J\b\u0010\u0014\u001a\u00020\u0013H\u0002J\b\u0010\u0015\u001a\u00020\u0013H\u0002J\b\u0010\u0016\u001a\u00020\u0013H\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0018"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/ActivityFeedViewModel;", "Landroidx/lifecycle/ViewModel;", "pdvClient", "Lorg/pcfx/client/c1/data/network/PDVClient;", "settingsPreferences", "Lorg/pcfx/client/c1/data/preferences/SettingsPreferences;", "(Lorg/pcfx/client/c1/data/network/PDVClient;Lorg/pcfx/client/c1/data/preferences/SettingsPreferences;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lorg/pcfx/client/c1/ui/viewmodel/ActivityFeedState;", "autoRefreshJob", "Lkotlinx/coroutines/Job;", "lastTimestamp", "", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadActivity", "", "observeSettings", "startAutoRefresh", "stopAutoRefresh", "Companion", "ClientC1_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ActivityFeedViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.network.PDVClient pdvClient = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.preferences.SettingsPreferences settingsPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<org.pcfx.client.c1.ui.viewmodel.ActivityFeedState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.ActivityFeedState> state = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job autoRefreshJob;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> lastTimestamp = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ActivityFeedViewModel";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.client.c1.ui.viewmodel.ActivityFeedViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public ActivityFeedViewModel(@org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.network.PDVClient pdvClient, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.preferences.SettingsPreferences settingsPreferences) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.ActivityFeedState> getState() {
        return null;
    }
    
    private final void observeSettings() {
    }
    
    public final void loadActivity() {
    }
    
    private final void startAutoRefresh() {
    }
    
    private final void stopAutoRefresh() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/ActivityFeedViewModel$Companion;", "", "()V", "TAG", "", "ClientC1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}