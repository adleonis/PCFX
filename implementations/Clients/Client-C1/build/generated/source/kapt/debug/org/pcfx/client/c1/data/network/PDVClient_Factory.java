package org.pcfx.client.c1.data.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class PDVClient_Factory implements Factory<PDVClient> {
  @Override
  public PDVClient get() {
    return newInstance();
  }

  public static PDVClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PDVClient newInstance() {
    return new PDVClient();
  }

  private static final class InstanceHolder {
    private static final PDVClient_Factory INSTANCE = new PDVClient_Factory();
  }
}
