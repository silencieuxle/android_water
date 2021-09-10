package me.gndev.water_battle.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.gndev.water_battle.data.database.WaterDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object BaseModules {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        val masterKeyBuilder = MasterKey.Builder(context)
        val masterKeyAlias = masterKeyBuilder.setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        return EncryptedSharedPreferences.create(
            context,
            "secured_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideWaterDatabase(@ApplicationContext context: Context): WaterDatabase =
        Room.databaseBuilder(context, WaterDatabase::class.java, "WaterDatabase").build()
}