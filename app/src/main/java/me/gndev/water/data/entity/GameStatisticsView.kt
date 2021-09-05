package me.gndev.water.data.entity

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT games.id as gameId, games.date as gameDate, games.score as gameScore," +
    "turns.time as turnTime, turns.volume as turnVolume" +
    "FROM games INNER JOIN turns ON turns.gameId = games.id"
)
data class GameStatisticsView (
    val gameId: Int,
    val gameDate: Long,
    val turnTime: Long,
    val gameScore: Long,
    val turnVolume: Int
)