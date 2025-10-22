package org.pcfx.client.c1.di;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import org.pcfx.client.c1.data.preferences.SettingsPreferences;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideSettingsPreferencesFactory implements Factory<SettingsPreferences> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideSettingsPreferencesFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsPreferences get() {
    return provideSettingsPreferences(contextProvider.get());
  }

  public static AppModule_ProvideSettingsPreferencesFactory create(
      Provider<Context> contextProvider) {
    return new AppModule_ProvideSettingsPreferencesFactory(contextProvider);
  }

  public static SettingsPreferences provideSettingsPreferences(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSettingsPreferences(context));
  }
}
