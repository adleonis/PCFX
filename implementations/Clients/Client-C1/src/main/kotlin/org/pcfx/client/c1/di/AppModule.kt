package org.pcfx.client.c1.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.pcfx.client.c1.data.network.PDVClient
import org.pcfx.client.c1.data.preferences.SettingsPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePDVClient(): PDVClient {
        return PDVClient().apply {
            initialize()
        }
    }

    @Singleton
    @Provides
    fun provideSettingsPreferences(
        @ApplicationContext context: Context
    ): SettingsPreferences {
        return SettingsPreferences(context)
    }
}
