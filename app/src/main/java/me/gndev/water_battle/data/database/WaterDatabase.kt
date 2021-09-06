package me.gndev.water_battle.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.gndev.water_battle.data.dao.GameDao
import me.gndev.water_battle.data.dao.StatisticsDao
import me.gndev.water_battle.data.dao.TurnDao
import me.gndev.water_battle.data.entity.Game
import me.gndev.water_battle.data.entity.GameStatisticsView
import me.gndev.water_battle.data.entity.Turn

@Database(
    entities = [Game::class, Turn::class],
    views = [GameStatisticsView::class], version = 1, exportSchema = false
)
abstract class WaterDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun turnDao(): TurnDao
    abstract fun statisticsDao(): StatisticsDao
}