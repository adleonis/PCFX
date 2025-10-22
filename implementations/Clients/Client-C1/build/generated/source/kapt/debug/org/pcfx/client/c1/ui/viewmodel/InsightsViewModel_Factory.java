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
public final class InsightsViewModel_Factory implements Factory<InsightsViewModel> {
  private final Provider<PDVClient> pdvClientProvider;

  public InsightsViewModel_Factory(Provider<PDVClient> pdvClientProvider) {
    this.pdvClientProvider = pdvClientProvider;
  }

  @Override
  public InsightsViewModel get() {
    return newInstance(pdvClientProvider.get());
  }

  public static InsightsViewModel_Factory create(Provider<PDVClient> pdvClientProvider) {
    return new InsightsViewModel_Factory(pdvClientProvider);
  }

  public static InsightsViewModel newInstance(PDVClient pdvClient) {
    return new InsightsViewModel(pdvClient);
  }
}
