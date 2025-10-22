package org.pcfx.client.c1.ui.viewmodel;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import org.pcfx.client.c1.data.network.PDVClient;
import org.pcfx.client.c1.data.preferences.SettingsPreferences;

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
public final class ActivityFeedViewModel_Factory implements Factory<ActivityFeedViewModel> {
  private final Provider<PDVClient> pdvClientProvider;

  private final Provider<SettingsPreferences> settingsPreferencesProvider;

  public ActivityFeedViewModel_Factory(Provider<PDVClient> pdvClientProvider,
      Provider<SettingsPreferences> settingsPreferencesProvider) {
    this.pdvClientProvider = pdvClientProvider;
    this.settingsPreferencesProvider = settingsPreferencesProvider;
  }

  @Override
  public ActivityFeedViewModel get() {
    return newInstance(pdvClientProvider.get(), settingsPreferencesProvider.get());
  }

  public static ActivityFeedViewModel_Factory create(Provider<PDVClient> pdvClientProvider,
      Provider<SettingsPreferences> settingsPreferencesProvider) {
    return new ActivityFeedViewModel_Factory(pdvClientProvider, settingsPreferencesProvider);
  }

  public static ActivityFeedViewModel newInstance(PDVClient pdvClient,
      SettingsPreferences settingsPreferences) {
    return new ActivityFeedViewModel(pdvClient, settingsPreferences);
  }
}
