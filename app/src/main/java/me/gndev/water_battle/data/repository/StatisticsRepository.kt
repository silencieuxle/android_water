package me.gndev.water_battle.data.repository

import me.gndev.water_battle.core.model.DataResult
import me.gndev.water_battle.data.dao.StatisticsDao
import me.gndev.water_battle.data.entity.GameStatisticsView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepository @Inject constructor(
    private val statisticsDao: StatisticsDao
) {
    suspend fun getGameStatisticsView(gameId: Int): DataResult<List<GameStatisticsView>> {
        return try {
            return DataResult.Success(statisticsDao.getGameStatistics(gameId))
        } catch (ex: Exception) {
            DataResult.Error(ex.message)
        }
    }
}