package me.gndev.water.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.gndev.water.data.dao.GameDao
import me.gndev.water.data.dao.StatisticsDao
import me.gndev.water.data.dao.TurnDao
import me.gndev.water.data.entity.Game
import me.gndev.water.data.entity.GameStatisticsView
import me.gndev.water.data.entity.Turn

@Database(
    entities = [Game::class, Turn::class],
    views = [GameStatisticsView::class], version = 1, exportSchema = false)
abstract class WaterDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun turnDao(): TurnDao
    abstract fun statisticsDao(): StatisticsDao
}