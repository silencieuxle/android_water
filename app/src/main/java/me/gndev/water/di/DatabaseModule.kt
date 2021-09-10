package me.gndev.water.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.gndev.water.data.dao.GameDao
import me.gndev.water.data.dao.StatisticsDao
import me.gndev.water.data.dao.TurnDao
import me.gndev.water.data.database.WaterDatabase

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideTurnDao(appDatabase: WaterDatabase): TurnDao = appDatabase.turnDao()

    @Provides
    fun provideGameDao(appDatabase: WaterDatabase): GameDao = appDatabase.gameDao()

    @Provides
    fun provideStatisticView(appDatabase: WaterDatabase): StatisticsDao =
        appDatabase.statisticsDao()
}