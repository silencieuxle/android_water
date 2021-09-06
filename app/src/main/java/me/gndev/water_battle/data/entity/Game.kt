package me.gndev.water_battle.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.gndev.water_battle.util.DateTimeUtils

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var date: Long = DateTimeUtils.getTodayAsNumber(),
    var score: Int = 0
)

data class GameWithTurns(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "id",
        entityColumn = "gameId"
    )
    val turns: List<Turn>
)