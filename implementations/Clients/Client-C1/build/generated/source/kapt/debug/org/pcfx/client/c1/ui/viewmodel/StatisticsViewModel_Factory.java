package org.pcfx.client.c1.ui.viewmodel;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import org.pcfx.client.c1.data.network.PDVClient;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class StatisticsViewModel_Factory implements Factory<StatisticsViewModel> {
  private final Provider<PDVClient> pdvClientProvider;

  public StatisticsViewModel_Factory(Provider<PDVClient> pdvClientProvider) {
    this.pdvClientProvider = pdvClientProvider;
  }

  @Override
  public StatisticsViewModel get() {
    return newInstance(pdvClientProvider.get());
  }

  public static StatisticsViewModel_Factory create(Provider<PDVClient> pdvClientProvider) {
    return new StatisticsViewModel_Factory(pdvClientProvider);
  }

  public static StatisticsViewModel newInstance(PDVClient pdvClient) {
    return new StatisticsViewModel(pdvClient);
  }
}
