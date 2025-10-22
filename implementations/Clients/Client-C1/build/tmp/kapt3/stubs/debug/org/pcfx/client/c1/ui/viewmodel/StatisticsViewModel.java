package org.pcfx.client.c1.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u000f"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/StatisticsViewModel;", "Landroidx/lifecycle/ViewModel;", "pdvClient", "Lorg/pcfx/client/c1/data/network/PDVClient;", "(Lorg/pcfx/client/c1/data/network/PDVClient;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lorg/pcfx/client/c1/ui/viewmodel/StatisticsState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadStatistics", "", "Companion", "ClientC1_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StatisticsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final org.pcfx.client.c1.data.network.PDVClient pdvClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<org.pcfx.client.c1.ui.viewmodel.StatisticsState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.StatisticsState> state = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "StatisticsViewModel";
    @org.jetbrains.annotations.NotNull()
    public static final org.pcfx.client.c1.ui.viewmodel.StatisticsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public StatisticsViewModel(@org.jetbrains.annotations.NotNull()
    org.pcfx.client.c1.data.network.PDVClient pdvClient) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<org.pcfx.client.c1.ui.viewmodel.StatisticsState> getState() {
        return null;
    }
    
    public final void loadStatistics() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/pcfx/client/c1/ui/viewmodel/StatisticsViewModel$Companion;", "", "()V", "TAG", "", "ClientC1_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}