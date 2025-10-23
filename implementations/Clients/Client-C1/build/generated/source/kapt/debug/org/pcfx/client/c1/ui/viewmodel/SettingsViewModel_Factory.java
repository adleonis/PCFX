package org.pcfx.client.c1.ui.viewmodel;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import org.pcfx.client.c1.data.network.PDVClient;
import org.pcfx.client.c1.data.preferences.SettingsPreferences;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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

  private final Provider<Context> contextProvider;

  public SettingsViewModel_Factory(Provider<SettingsPreferences> settingsPreferencesProvider,
      Provider<PDVClient> pdvClientProvider, Provider<Context> contextProvider) {
    this.settingsPreferencesProvider = settingsPreferencesProvider;
    this.pdvClientProvider = pdvClientProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsPreferencesProvider.get(), pdvClientProvider.get(), contextProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsPreferences> settingsPreferencesProvider,
      Provider<PDVClient> pdvClientProvider, Provider<Context> contextProvider) {
    return new SettingsViewModel_Factory(settingsPreferencesProvider, pdvClientProvider, contextProvider);
  }

  public static SettingsViewModel newInstance(SettingsPreferences settingsPreferences,
      PDVClient pdvClient, Context context) {
    return new SettingsViewModel(settingsPreferences, pdvClient, context);
  }
}
