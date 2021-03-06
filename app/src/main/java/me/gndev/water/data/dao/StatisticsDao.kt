package me.gndev.water.data.dao

import androidx.room.Dao
import androidx.room.Query
import me.gndev.water.data.entity.GameStatisticsView

@Dao
interface StatisticsDao {
    @Query("SELECT * FROM game_statistics_view WHERE gameId = :gameId")
    suspend fun getGameStatistics(gameId: Int): List<GameStatisticsView>
}