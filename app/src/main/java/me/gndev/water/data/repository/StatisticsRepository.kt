package me.gndev.water.data.repository

import me.gndev.water.core.model.DataResult
import me.gndev.water.data.dao.StatisticsDao
import me.gndev.water.data.entity.GameStatisticsView
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