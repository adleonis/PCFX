package org.pcfx.client.c1.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import org.pcfx.client.c1.data.network.PDVClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvidePDVClientFactory implements Factory<PDVClient> {
  @Override
  public PDVClient get() {
    return providePDVClient();
  }

  public static AppModule_ProvidePDVClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PDVClient providePDVClient() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePDVClient());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvidePDVClientFactory INSTANCE = new AppModule_ProvidePDVClientFactory();
  }
}
