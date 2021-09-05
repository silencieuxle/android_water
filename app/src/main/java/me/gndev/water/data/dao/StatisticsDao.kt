package me.gndev.water.data.dao

import androidx.room.Dao
import androidx.room.Query
import me.gndev.water.data.entity.GameStatisticsView

@Dao
interface StatisticsDao {
    @Query("SELECT * FROM GameStatisticsView WHERE gameId = :gameId")
    suspend fun getGameStatistics(gameId: Int): List<GameStatisticsView>
}