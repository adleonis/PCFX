package org.pcfx.client.c1.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\u000fH\u0002J\u0010\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u000e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0013J\u000e\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001bJ\u000e\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006 "}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsPreferences", "Lorg/pcfx/client/c1/data/preferences/SettingsPreferences;", "pdvClient", "Lorg/pcfx/client/c1/data/network/PDVClient;", "(Lorg/pcfx/client/c1/data/preferences/SettingsPreferences;Lorg/pcfx/client/c1/data/network/PDVClient;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lorg/pcfx/client/c1/ui/viewmodel/SettingsState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearMessage", "", "loadSettings", "performSearch", "query", "", "updateAutoRefresh", "enabled", "", "updatePdvHost", "host", "updatePdvPort", "port", "", "updateRefreshInterval", "intervalMs", "updateSearchQuery", "Companion", "ClientC1_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.preferences.SettingsPreferences settingsPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.network.PDVClient pdvClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<org.pcfx.client.c1.ui.viewmodel.SettingsState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.SettingsState> state = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "SettingsViewModel";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.client.c1.ui.viewmodel.SettingsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.preferences.SettingsPreferences settingsPreferences, @org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.network.PDVClient pdvClient) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.SettingsState> getState() {
        return null;
    }
    
    private final void loadSettings() {
    }
    
    public final void updatePdvHost(@org.jetbrains.annotations.NotNull()
    java.lang.String host) {
    }
    
    public final void updatePdvPort(int port) {
    }
    
    public final void updateAutoRefresh(boolean enabled) {
    }
    
    public final void updateRefreshInterval(int intervalMs) {
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    private final void performSearch(java.lang.String query) {
    }
    
    public final void clearMessage() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/SettingsViewModel$Companion;", "", "()V", "TAG", "", "ClientC1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}