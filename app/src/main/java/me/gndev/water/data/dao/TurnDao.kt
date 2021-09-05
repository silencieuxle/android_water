package me.gndev.water.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.gndev.water.data.entity.Turn
import java.util.*

@Dao
interface TurnDao {
    @Query("SELECT * FROM turns")
    suspend fun getAll(): List<Turn>

    @Query("SELECT * FROM turns WHERE gameId = :gameId")
    suspend fun getByGame(gameId: Int): List<Turn>

    @Query("SELECT * FROM turns WHERE date = :dateAsNumber")
    suspend fun getByDate(dateAsNumber: Long): List<Turn>

    @Query("SELECT * FROM turns WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Turn?

    @Insert
    suspend fun insert(turn: Turn)

    @Update
    suspend fun update(turn: Turn)

    @Query("DELETE FROM turns WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM turns")
    suspend fun clear()
}