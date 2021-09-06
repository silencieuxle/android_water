package me.gndev.water_battle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.gndev.water_battle.data.dao.GameDao
import me.gndev.water_battle.data.dao.StatisticsDao
import me.gndev.water_battle.data.dao.TurnDao
import me.gndev.water_battle.data.database.WaterDatabase

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