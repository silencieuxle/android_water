package me.gndev.water.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.gndev.water.core.room_converter.Converters
import me.gndev.water.data.database.WaterDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object BaseModules {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideWaterDatabase(@ApplicationContext context: Context): WaterDatabase =
        Room.databaseBuilder(context, WaterDatabase::class.java, "WaterDatabase").build()
}