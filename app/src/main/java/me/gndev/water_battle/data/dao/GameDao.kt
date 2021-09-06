package me.gndev.water_battle.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.gndev.water_battle.data.entity.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    suspend fun getAll(): List<Game>

    @Query("SELECT * FROM games WHERE date = :dateAsNumber")
    suspend fun getByDate(dateAsNumber: Long): List<Game>

    @Query("SELECT * FROM games ORDER BY id DESC LIMIT 1")
    suspend fun getToday(): Game?

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Game

    @Query("SELECT EXISTS (SELECT 1 FROM games WHERE date = :date)")
    suspend fun exists(date: Long): Boolean

    @Insert
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Query("DELETE FROM games WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM games")
    suspend fun clear()
}