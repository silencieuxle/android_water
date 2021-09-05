package me.gndev.water.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.gndev.water.data.share_preferences.PrefManager
import me.gndev.water.data.share_preferences.PrefManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SharePreferencesModule {
    @Singleton
    @Binds
    abstract fun provideSharedPreferenceManager(prefManagerImpl: PrefManagerImpl): PrefManager
}
