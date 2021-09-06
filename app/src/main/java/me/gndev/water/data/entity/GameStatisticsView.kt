package me.gndev.water.data.entity

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT games.id AS gameId, games.date AS gameDate, games.score AS gameScore, " +
    "turns.time AS turnTime, turns.volume AS turnVolume " +
    "FROM games INNER JOIN turns ON turns.gameId = games.id"
)
data class GameStatisticsView (
    val gameId: Int,
    val gameDate: Long,
    val turnTime: Long,
    val gameScore: Long,
    val turnVolume: Int
)