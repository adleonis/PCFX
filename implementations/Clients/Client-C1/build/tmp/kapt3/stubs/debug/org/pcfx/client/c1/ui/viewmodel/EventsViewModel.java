package org.pcfx.client.c1.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0010\u0010\u0010\u001a\u00020\r2\b\u0010\u0011\u001a\u0004\u0018\u00010\u000fJ\u0018\u0010\u0012\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00150\u00140\u0013J\u0006\u0010\u0016\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0018"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/EventsViewModel;", "Landroidx/lifecycle/ViewModel;", "pdvClient", "Lorg/pcfx/client/c1/data/network/PDVClient;", "(Lorg/pcfx/client/c1/data/network/PDVClient;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lorg/pcfx/client/c1/ui/viewmodel/EventsState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "filterByContentKind", "", "kind", "", "filterBySurface", "surface", "getFilteredEvents", "", "", "", "loadEvents", "Companion", "ClientC1_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class EventsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.network.PDVClient pdvClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<org.pcfx.client.c1.ui.viewmodel.EventsState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.EventsState> state = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "EventsViewModel";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.client.c1.ui.viewmodel.EventsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public EventsViewModel(@org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.network.PDVClient pdvClient) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.EventsState> getState() {
        return null;
    }
    
    public final void loadEvents() {
    }
    
    public final void filterByContentKind(@org.jetbrains.annotations.Nullable()
    java.lang.String kind) {
    }
    
    public final void filterBySurface(@org.jetbrains.annotations.Nullable()
    java.lang.String surface) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> getFilteredEvents() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/EventsViewModel$Companion;", "", "()V", "TAG", "", "ClientC1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}