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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsPreferences> settingsPreferencesProvider;

  private final Provider<PDVClient> pdvClientProvider;

  public SettingsViewModel_Factory(Provider<SettingsPreferences> settingsPreferencesProvider,
      Provider<PDVClient> pdvClientProvider) {
    this.settingsPreferencesProvider = settingsPreferencesProvider;
    this.pdvClientProvider = pdvClientProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsPreferencesProvider.get(), pdvClientProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsPreferences> settingsPreferencesProvider,
      Provider<PDVClient> pdvClientProvider) {
    return new SettingsViewModel_Factory(settingsPreferencesProvider, pdvClientProvider);
  }

  public static SettingsViewModel newInstance(SettingsPreferences settingsPreferences,
      PDVClient pdvClient) {
    return new SettingsViewModel(settingsPreferences, pdvClient);
  }
}
