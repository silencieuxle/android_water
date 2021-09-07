package me.gndev.water_battle.data.entity

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT games.id AS gameId, games.date AS gameDate, games.score AS gameScore, " +
            "turns.time AS turnTime, turns.volume AS turnVolume, turns.weapon as turnWeapon " +
            "FROM games INNER JOIN turns ON turns.gameId = games.id",
    viewName = "game_statistics_view"
)
data class GameStatisticsView(
    val gameId: Int,
    val gameDate: Long,
    val turnTime: Long,
    val gameScore: Long,
    val turnVolume: Int,
    val turnWeapon: String,
)