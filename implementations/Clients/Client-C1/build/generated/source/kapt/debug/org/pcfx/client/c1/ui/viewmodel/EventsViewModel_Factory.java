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
public final class EventsViewModel_Factory implements Factory<EventsViewModel> {
  private final Provider<PDVClient> pdvClientProvider;

  public EventsViewModel_Factory(Provider<PDVClient> pdvClientProvider) {
    this.pdvClientProvider = pdvClientProvider;
  }

  @Override
  public EventsViewModel get() {
    return newInstance(pdvClientProvider.get());
  }

  public static EventsViewModel_Factory create(Provider<PDVClient> pdvClientProvider) {
    return new EventsViewModel_Factory(pdvClientProvider);
  }

  public static EventsViewModel newInstance(PDVClient pdvClient) {
    return new EventsViewModel(pdvClient);
  }
}
